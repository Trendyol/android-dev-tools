## Input Autofill (MVP - In Development)

This library allows developer and testers to fill form inputs automatically with predefined
form data.

### Screenshots

<table>
 	<tr>
  		<td><img src="/autofill/art/ss1.png" width="250" /></td>
  		<td><img src="/autofill/art/ss2.png" width="250" /></td>
  		<td><img src="/autofill/art/ss3.png" width="250" /></td>
 	</tr>
</table>

### How it works?

1. It observes activity and fragment lifecycle events from the application class to detect completed layout renderings.
2. After each render, it searches for inputs which has certain input types like email/phone/password.
3. If the layout has required inputs, it shows a snackBar on the activity view to continue with autofill.
4. After clicking the snackBar action, it opens a single-select list dialog to choose a autofill data.

### Setup

```kotlin
AutofillService.Builder(this)
    .withAutoFillData(
        listOf(
            AutofillData.LoginEmail("test@trendyol.com", "123456"),
            AutofillData.LoginEmail("guest@trendyol.com", "123456"),
            AutofillData.LoginEmail("dev@trendyol.com", "123456"),
            AutofillData.LoginPhone("+90 506 643 1212", "123456"),
        )
    )
    .build()
```

## Create Custom Autofill Data

Creating Autofill Data
```kotlin
class RegisterAutofillData(
    private val email: String,
    private val name: String,
    private val password: String,
) : AutofillData(RegisterInputAdapter(email, name, password), email)
```

Creating Input Adapter
```kotlin
class RegisterInputAdapter(
    private val email: String,
    private val firstname: String,
    private val password: String,
) : InputAdapter() {

    override val inputTypes: Map<Int, String>
        get() = mapOf(
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS to email,
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME to firstname,
            InputType.TYPE_TEXT_VARIATION_PASSWORD to password
        )
}
```

### TODO

- [ ] Autofill by Input Res ID to support complex forms
- [ ] Code documentation
- [ ] Unit tests
