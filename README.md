## Android Dev Tools

Android Dev Tools is a library that contains various QA/Debug tools to speed up and streamline the development progress.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Tools ##
* [Autofill Service](#autofill-service)
* [Analytics Logger](#analytics-logger)
* [Environment Manager](#environment-manager)
* [Debug Menu](#debug-menu)

## Autofill Service
Autofill Service allows developers and QA to fill form inputs automatically with predefined form data.

### How it Works?
It observes both activity and fragment lifecycle events via application class to detect layout inflations.
After each inflation, it seeks for determined input views in the inflated layout.
If it has all required inputs, then shows the autofill action.
Autofill data that suitable with inflated form inputs are shown in the selection dialog.

### Demo
<table>
 	<tr>
  		<td><img src="/art/autofill_service_1.png" width="200" /></td>
  		<td><img src="/art/autofill_service_2.png" width="200" /></td>
  		<td><img src="/art/autofill_service_3.png" width="200" /></td>
  		<td><img src="/art/autofill_service_4.png" width="200" /></td>
 	</tr>
</table>

### Setup
```gradle
dependencies {
    debugImplementation ''
}
```

## Analytics Logger
It allows to log & inspect sent analytics events.

### Demo
<table>
 	<tr>
  		<td><img src="/art/analytics_logger_1.png" width="200" /></td>
  		<td><img src="/art/analytics_logger_2.png" width="200" /></td>
  		<td><img src="/art/analytics_logger_3.png" width="200" /></td>
 	</tr>
</table>

### Usage
```kotlin
    AnalyticsLogger.init(this)
    
    AnalyticsLogger.report(
        key = "eventKey",
        value = EventModel("TestAction", "TestCategory", "TestScreen"), // :Any
        platform = "EventPlatform",
    )
```

### Setup
```gradle
dependencies {
    debugImplementation ''
}
```

## Environment Manager
It provides environment selection dialog can be opened from the app notifications with predefined environment data.

### Setup
```gradle
dependencies {
    debugImplementation ''
}
```

## Debug Menu
It provides a debug page build with predefined custom action and events.

### Setup
```gradle
dependencies {
    debugImplementation ''
}
```

License
--------


    Copyright 2022 Trendyol.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
