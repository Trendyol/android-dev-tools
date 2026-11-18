package com.trendyol.android.devtools.autofillservice.internal

import android.os.Looper
import android.view.View
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.viewinterop.AndroidView
import com.trendyol.android.devtools.autofillservice.internal.ext.findAllInputs
import com.trendyol.android.devtools.autofillservice.internal.lifecycle.AutofillInputObserver
import com.trendyol.android.devtools.autofillservice.internal.model.ListItem
import com.trendyol.android.devtools.autofillservice.internal.ui.AutofillDialog
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
@LooperMode(LooperMode.Mode.PAUSED)
class ComposeAutofillTest {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    private lateinit var controller: ActivityController<ComponentActivity>
    private lateinit var activity: ComponentActivity
    private val root: View get() = activity.findViewById(android.R.id.content)

    @Before
    fun setUp() {
        composeRule.mainClock.autoAdvance = false
        controller = Robolectric.buildActivity(ComponentActivity::class.java).setup()
        activity = controller.get()
    }

    @After
    fun tearDown() {
        controller.pause().stop().destroy()
    }

    @Test
    fun `tagged standard text fields update their Compose state`() {
        val basic = mutableStateOf("")
        val material = mutableStateOf("")
        val outlined = mutableStateOf("")
        render {
            Column {
                BasicText("Heading", Modifier.testTag("heading"))
                BasicTextField(basic.value, { basic.value = it }, Modifier.testTag("basic"))
                TextField(material.value, { material.value = it }, Modifier.testTag("material"))
                OutlinedTextField(outlined.value, { outlined.value = it }, Modifier.testTag("outlined"))
                BasicTextField("untagged", {})
            }
        }

        val inputs = root.findAllInputs().associateBy { it.id }
        assertEquals(setOf("basic", "material", "outlined"), inputs.keys)
        assertTrue(inputs.values.all { it.isShown })
        inputs.forEach { (id, input) -> input.fill("filled $id") }

        assertEquals("filled basic", basic.value)
        assertEquals("filled material", material.value)
        assertEquals("filled outlined", outlined.value)
        advanceFrame()
        assertEquals(inputs.mapValues { "filled ${it.key}" }, root.findAllInputs().associate { it.id to it.value })
    }

    @Test
    fun `passwords can be filled without saving masked history and noneditable fields are skipped`() {
        val password = mutableStateOf("")
        render {
            Column {
                OutlinedTextField(
                    password.value,
                    { password.value = it },
                    Modifier.testTag("password"),
                    visualTransformation = PasswordVisualTransformation(),
                )
                BasicTextField("disabled", {}, Modifier.testTag("disabled"), enabled = false)
                BasicTextField("read only", {}, Modifier.testTag("readOnly"), readOnly = true)
            }
        }

        val input = root.findAllInputs().single()
        assertEquals("password", input.id)
        input.fill("secret")
        assertEquals("secret", password.value)
        advanceFrame()
        assertNull(root.findAllInputs().single().value)
    }

    @Test
    fun `Compose and AndroidView inputs are discovered together and anonymous views are skipped`() {
        val email = mutableStateOf("")
        render {
            Column {
                BasicTextField(email.value, { email.value = it }, Modifier.testTag("email"))
                AndroidView(factory = { EditText(it).apply { id = android.R.id.edit } })
                AndroidView(factory = { EditText(it) })
            }
        }

        val inputs = root.findAllInputs().associateBy { it.id }
        assertEquals(setOf("email", "edit"), inputs.keys)
        inputs.getValue("email").fill("test@example.com")
        inputs.getValue("edit").fill("view value")
        advanceFrame()
        assertEquals("test@example.com", email.value)
        assertEquals("view value", root.findAllInputs().first { it.id == "edit" }.value)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `fields hidden by ancestor semantics are not filled`() {
        render {
            Column(Modifier.semantics { invisibleToUser() }) {
                BasicTextField("hidden", {}, Modifier.testTag("hidden"))
            }
        }
        assertFalse(root.findAllInputs().single().isShown)
    }

    @Test
    fun `observer detects delayed fields and navigation without repeating on text changes`() {
        val visible = mutableStateOf(false)
        val email = mutableStateOf("")
        val changes = mutableListOf<Set<String>>()
        val removed = mutableListOf<Map<String, String?>>()
        render {
            if (visible.value) {
                BasicTextField(email.value, { email.value = it }, Modifier.testTag("email"))
            }
        }
        val observer = AutofillInputObserver(root, { changes.add(it.keys) }, { removed.add(it) })
        try {
            assertTrue("The observer needs an attached activity root", root.isAttachedToWindow)
            visible.value = true
            advanceFrame()
            assertEquals(setOf("email"), root.findAllInputs().map { it.id }.toSet())
            assertEquals(listOf(setOf("email")), changes)
            val identity = observer.inputs.getValue("email").identity

            observer.inputs.getValue("email").fill("last@example.com")
            advanceFrame()
            assertEquals(1, changes.size)

            visible.value = false
            advanceFrame()
            assertEquals(listOf(setOf("email"), emptySet<String>()), changes)
            assertEquals(mapOf("email" to "last@example.com"), removed.last())

            visible.value = true
            advanceFrame()
            assertEquals(3, changes.size)
            assertFalse(identity == observer.inputs.getValue("email").identity)
        } finally {
            observer.dispose()
        }
    }

    @Test
    fun `picker works in a ComponentActivity without a fragment manager`() {
        val dialog = AutofillDialog(activity)
        dialog.updateItems(listOf(ListItem.Category("Test users")))
        dialog.show()
        assertTrue(dialog.isShowing)
        dialog.dismiss()
    }

    private fun render(content: @Composable () -> Unit) {
        activity.setContent { MaterialTheme { content() } }
        advanceFrame()
    }

    private fun advanceFrame() {
        Snapshot.sendApplyNotifications()
        composeRule.mainClock.advanceTimeBy(32)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        root.measure(
            View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY),
        )
        root.layout(0, 0, 1080, 1920)
        composeRule.mainClock.advanceTimeBy(32)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        root.viewTreeObserver.dispatchOnPreDraw()
    }
}
