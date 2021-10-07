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

### TODO

- [ ] Code documentation
- [ ] Unit tests
