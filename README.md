## Android Dev Tools

Android Dev Tools is a library that contains various QA/Debug tools to speed up and streamline the development progress.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Tools ##
* [Autofill Service](#autofill-service)
* [Analytics Logger](#analytics-logger)
* [Http Inspector (Alpha)](#http-inspector-alpha)
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
    debugImplementation "com.trendyol"
}
```

## Analytics Logger
It allows to log & inspect analytics events sent by client.

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
    debugImplementation "com.trendyol"
}
```

## Http Inspector (Alpha)
It provides an OkHttp interceptor and web interface to inspect, manipulate in realtime and mock HTTP request and responses.

### Manipulating Responses
<table>
 	<tr>
  		<td><img src="/art/http_inspector_1.png" width="400" /></td>
 	</tr>
</table>
It allows us to manipulate OkHttp request/response data via web interface.

### How it Works
<img src="/art/http_inspector_4.png" width="650" />
Any request passing through the interceptor is held in the request queue to sync all of them.
Held requests are being sent to the web interface via Ktor webserver in order.
After doing any changes in the response data, it waits for the acceptance, then sends manipulated response back to the request queue.
The actual response data is replaced with the manipulated response and reflected to the application.

### Mocking Requests
<table>
 	<tr>
  		<td><img src="/art/http_inspector_2.png" width="400" /></td>
  		<td><img src="/art/http_inspector_3.png" width="400" /></td>
 	</tr>
</table>
With this feature, we can imitate the API by preparing mock request and response data for the REST API that has not been prepared yet.
It provides a web interface where we can enter mock request and response data, and allows us to enable/disable the previously added mock data.
The point to be considered is which requests will be answered with mock data rather than going to the real API is decided by comparing the URL, method and request body data of the request in the real request and mock data. If this data is completely matched, the mock response will be served to the client.

### Usage
```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(MockInterceptor(context))
    .build()
```

### Setup
```gradle
dependencies {
    debugImplementation "com.trendyol"
}
```

## Environment Manager
It provides environment selection dialog can be opened from the app notifications with predefined environment data.

### Setup
```gradle
dependencies {
    debugImplementation "com.trendyol"
}
```

## Debug Menu
It provides a debug page build with predefined custom action and events.

### Setup
```gradle
dependencies {
    debugImplementation "com.trendyol"
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
