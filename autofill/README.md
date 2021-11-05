## Input Autofill

This library allows developer and testers to fill form inputs automatically with predefined
form data.

### Screenshots

<table>
 	<tr>
  		<td><img src="/autofill/art/gif.gif" width="320" /></td>
 	</tr>
</table>

### How it works?

1. It observes activity and fragment lifecycle events from the application class to detect completed layout renderings.
2. After each render, it searches for inputs which has certain input id's described in the asset file.
3. If the layout has required inputs, it shows a snackBar on the activity view to continue with autofill.
4. After clicking the snackBar action, it opens a dialog to choose a autofill category or item.
5. After filling the required inputs, it records the input values to show next selection as autofill item.

### Setup

```kotlin
AutofillService.Builder(this)
    .withFilePath("autofill.json")
    .build()
```

### Creating Asset File

```json
{
  "forms": [
    {
      "fields": ["inputEmail", "inputPassword"],
      "categories": {
        "Black Wallet": [
          ["test@trendyol.com", "123456"],
          ["meal@trendyol.com", "123456"]
        ],
        "Consumer Lending": [
          ["test@trendyol.com", "123456"],
          ["meal@trendyol.com", "123456"]
        ]
      }
    },
    {
      "fields": ["inputCardNo", "inputCardMonth", "inputCardYear", "inputCardCvv"],
      "categories": {
        "Some Feature": [
          ["1234 1234 1234 1234", "12", "21", "123"],
          ["1111 1111 1111 1111", "12", "21", "123"]
        ],
        "Some Feature 2": [
          ["1234 1234 1234 1234", "12", "21", "123"],
          ["1111 1111 1111 1111", "12", "21", "123"]
        ]
      }
    }
  ]
}
```

### TODO

- [x] Autofill by Input Res ID to support complex forms
- [x] Select from autofill history
- [ ] Code documentation
- [ ] Unit tests
