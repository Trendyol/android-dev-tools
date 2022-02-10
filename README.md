## Android Dev Tools

Android Dev Tools is a library that contains various QA/Debug tools to speed up and streamline the development progress.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Tools ##
* [Autofill Service](#autofill-service)
* [Analytics Logger](#analytics-logger)
* [Http Inspector](#http-inspector)
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

### Usage
```kotlin
AutofillService.Builder(this)
    .withFilePath("autofill.json")
    .build()
```

### Configuration
Configuration Json file can be located in /assets folder. You can define autofill data by following this structure.
You should also note that the order of the defined form field resource id's and order of input values must match.
```json
{
  "forms": [
    {
      "fields": ["inputEmail", "inputPassword"], // Form input resource id's
      "categories": {
        "Temporary Users": [
          { "description": "Has more then one order history.", "values": ["test@mail.com", "123456"] },
          { "description": "Has more then one order history.", "values": ["meal@mail.com", "123456"] },
          { "description": "Has more then one order history.", "values": ["dev@mail.com", "123456"] },
          { "description": "Has more then one order history.", "values": ["tools@mail.com", "123456"] }
        ],
        "Test Users": [
          { "description": "Has more then one order history.", "values": ["test@mail.com", "123456"] },
          { "description": "Has more then one order history.", "values": ["meal@mail.com", "123456"] },
          { "description": "Has more then one order history.", "values": ["dev@mail.com", "123456"] },
          { "description": "Has more then one order history.", "values": ["tools@mail.com", "123456"] }
        ]
      }
    }
  ]
}
```

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

## Http Inspector
It provides an OkHttp interceptor and web interface to inspect, manipulate in realtime and mock HTTP request and responses.

### Manipulating Responses
Initially, the request manipulation is not active and requests sent from the client won't be blocked.
By pressing the "Toggle Auto Skip" button, we can start blocking the requests sent from the client.
Blocked requests will shown in the web interface in order and will allow manipulating the response data from the panel.
During this period, other requests sent will be held in a queue on the client.
The next request will be sent to the interface only after the current one is confirmed.
Thus, all requests will be synced and it will be possible to manipulate all of them.

### Mocking Requests
With this feature, we can imitate the API by preparing mock request and response data for the REST API that has not been prepared yet.
It provides a web interface where we can enter mock request and response data, and allows us to enable/disable the previously added mock data.
The point to be considered is which requests will be answered with mock data rather than going to the real API is decided by comparing the URL, method and request body data of the request in the real request and mock data. If this data is completely matched, the mock response will be served to the client.

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
