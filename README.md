# Ametista-Engine

![Maven Central](https://img.shields.io/maven-central/v/io.github.n7ghtm4r3/Ametista-Engine.svg?label=Maven%20Central)

![Static Badge](https://img.shields.io/badge/android-4280511051?link=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dcom.tecknobit.ametista)
![Static Badge](https://img.shields.io/badge/ios-445E91?link=https%3A%2F%2Fimg.shields.io%2Fbadge%2Fandroid-4280511051)
![Static Badge](https://img.shields.io/badge/desktop-006874?link=https%3A%2F%2Fimg.shields.io%2Fbadge%2Fandroid-4280511051)
![Static Badge](https://img.shields.io/badge/wasmjs-834C74?link=https%3A%2F%2Fimg.shields.io%2Fbadge%2Fandroid-4280511051)

**v1.0.1**

This project, based on Java and the Spring Boot framework, is an open source self-hosted issues tracker and performance
stats collector about Compose Multiplatform applications

Improve the quality and stability of your apps with **Ametista**!

This repository contains the engine of **Ametista**, so if you want to customize you can fork
it and work on it, if there are any errors, fixes to do or some idea to upgrade this project, please
open a ticket or contact us to talk about, thanks and good use!

## 🛠 Skills

- Java
- Kotlin

## Architecture

### Engine

It is the core component of **Ametista**. It collects performance data and tracks issues to send to your backend
instance for analysis.

### Clients

This project will be constantly developed to reach different platforms to work on, following the platforms releases
steps:

- Mobile
  - <a href="https://play.google.com/store/apps/details?id=com.tecknobit.ametista">Android</a>
  - iOS -> planned
- <a href="https://github.com/N7ghtm4r3/Ametista-Clients/releases/tag/1.0.1">Ametista desktop version</a>

### Backend

- <a href="https://github.com/N7ghtm4r3/Ametista/releases/tag/1.0.1">Backend service "out-of-the-box"</a>

## Core functionality

### Issues tracking

When an error occurs during the runtime of your application, it will be automatically caught and sent to your server
instance for tracking. It keeps tracks
also of the different versions of the application

### Performance monitoring

- Launch time: will be automatically detected by the Engine and sent to your server instance
- Network requests count: you have to integrate it if you need this analytic
- Total issues: will be automatically counted by the Engine
  - Issues per session: related to total issues analytic will be automatically calculated

## Integration

### Implementation

#### Version catalog

```gradle
[versions]
ametista-engine = "1.0.1"

[libraries]
ametista-engine = { module = "io.github.n7ghtm4r3:Ametista-Engine", version.ref = "ametista-engine" } 
```

#### Gradle

Add the JitPack repository to your build file

- Add it in your root build.gradle at the end of repositories
         
    ```gradle
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
    ```

    #### Gradle (Kotlin)
         
    ```gradle
    repositories {
        ...
        maven("https://jitpack.io")
    }
    ```
    
- Add the dependency
         
    ```gradle
    dependencies {
        implementation 'io.github.n7ghtm4r3:Ametista-Engine:1.0.1'
    }
    ```

    #### Gradle (Kotlin)
         
    ```gradle
    dependencies {
        implementation("io.github.n7ghtm4r3:Ametista-Engine:1.0.1")
    }
    ```

    #### Gradle (version catalog)
         
    ```gradle
    dependencies {
        implementation(libs.ametista.engine)
    }
    ```

### Configuration file

To send the issues and the performance analytics to your backend you have to create the related **"ametista.config"**
configuration file:

```json
{
  "host": "your_host_address",
  "server_secret": "your_server_secret",
  "application_id": "your_application_id",
  "bypass_ssl_validation": "true [default false]",
  // whether bypass the SSL certificates validation, this for example when is a self-signed the certificate USE WITH CAUTION
  "app_version": "X.Y.Z",
  // required if any specific versions are not specified
  // general version to use if the specific one for a target is not specified
  "android": {
    // not required
    "app_version": "Y.X.Z"
    // specific target version to use
  },
  "ios": {
    // not required
    "app_version": "Y.X.Z"
    // specific target version to use
  },
  "desktop": {
    // not required
    "app_version": "Y.X.Z"
    // specific target version to use
  },
  "web": {
    // not required
    "app_version": "Y.X.Z"
    // specific target version to use
  }
}
```

> [!CAUTION]
>
> This file contains **sensitive information** such as server addresses, server secrets, and application-specific data.
>
> **To protect this data:**
> - **Do not share** this file in any public locations, such as public repositories, forums, or websites.
> - **Exclude** this file from version control if working in a public repository (e.g., using `.gitignore` for Git).
> - **Limit access** to this file to authorized personnel only.
> - **Use environment variables** or a secrets management tool instead of hardcoding sensitive data, where possible.

### Intake the engine

The first step to start the engine is intake it in each main of your application targets platform

#### Android

Intake the engine in your **MainActivity**

```kotlin
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // invoke it before the App() invocation
    AmetistaEngine.intake()

    setContent {
      App()
    }

  }
}
```

#### Ios

Intake the engine in your **MainViewController.kt** script file

```kotlin
fun MainViewController() {

  // invoke it before the App() invocation
  AmetistaEngine.intake()

  ComposeUIViewController {
    App()
  }
}
```

#### Desktop

Intake the engine in your **main.kt**

```kotlin
fun main() {
  // invoke it before the App() invocation
  AmetistaEngine.intake()

  application {
    Window(
      onCloseRequest = ::exitApplication,
      title = "app_title",
    ) {
      App()
    }
  }
}
```

#### Web (WasmJs)

Intake the engine in your **main.kt**

```kotlin
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
  // invoke it before the App() invocation
  AmetistaEngine.intake()

  ComposeViewport(document.body!!) {
    App()
  }
}
```

### Engine's fire-up

The next, and the last, step is fire-up the engine.
You have three different options to fire-up it, but the only requirement is place the fire-up procedure in your
**App.kt** script file

#### Custom configuration path

You can locate the **"ametista.config"** file in custom path, but the structure of the file must be the same, then
fire-up with
the dedicated method:

```kotlin
fun App() {
  val ametistaEngine = AmetistaEngine.ametistaEngine
  ametistaEngine.fireUp(
    configPath = "your_configuration_file_path", // file is not located the common resources
    loggingEnabled = false, // whether to enable the logging of the engine operations
    debugMode = false // whether the issues or the stats collected have to real counted or just simulated their sent,
    // make sure that in production is set on *false*
  )
  MaterialTheme {
    Column(
      modifier = Modifier
        .fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Hello World!"
      )
    }
  }
}
```

#### Configuration file in common resources

If your application has multiple targets you can locate the configuration file in
the  [common resources](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources.html#access-the-available-resources-in-your-code):

``` bash
  commonMain
  |-- composeResources
      |-- files
          |-- ametista.config
  ```

then fire-up the engine with the dedicated method:

```kotlin
@Composable
fun App() {
  LaunchedEffect(Unit) {
    val ametistaEngine = AmetistaEngine.ametistaEngine
    ametistaEngine.fireUp(
      configData = Res.readBytes(FILES_AMETISTA_CONFIG_PATHNAME), // files/ametista.config pathname
      loggingEnabled = false, // whether to enable the logging of the engine operations
      debugMode = false // whether the issues or the stats collected have to real counted or just simulated their sent,
      // make sure that in production is set on *false*
    )
  }
  MaterialTheme {
    Column(
      modifier = Modifier
        .fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Hello World!"
      )
    }
  }
}
```

### Network requests count (if needed)

To enable the tracking of this analytics, you must integrate the Engine into your HTTP client (or a similar component)
to count the requests.  
For example, you can do this by adding an interceptor, such as with [Ktor](https://ktor.io/docs/client-http-send.html)
or [OkHttp](https://square.github.io/okhttp/features/interceptors/).
Independently of your HTTP client the Engine integration is the following:

```kotlin
...
your_interceptor_scope {
  val ametistaEngine = AmetistaEngine.ametistaEngine
  ametistaEngine.notifyNetworkRequest() // will be automatically sent and counted
}
...
```

And that's it! The Engine is fully integrated on your clients and working in coroutines it will not affect your
application main workflow

## Connection procedure

### Invocation 

To connect the specific platform for your application you need to invoke the related method as following:

```kotlin
@Composable
fun App() {
    val ametistaEngine = AmetistaEngine.ametistaEngine
    
    // after engine initialized
    
    ametistaEngine.connectPlatform() // running on Desktop for example

    MaterialTheme {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello World!"
            )
        }
    }
}
```

### Connection phase

The method automatically detects the platform on which the application it is launched and connects with it. For example, if launched on a desktop, it will connect with the Desktop platform and so on for other platforms.

### Platform connected

Once the platform has been connected, you can remove the method invocation for that platform to prevent retry attempts to connect to the same platform, or you can connect other platforms and follow the same procedure phases

## Privacy Policy

If you need to inform the users about the tracking activity or performance monitoring of the Ametista-Engine you can
use this [template](https://github.com/N7ghtm4r3/Ametista-Engine/blob/main/POLICY.md) and customize it as you need

## Authors

- [@N7ghtm4r3](https://www.github.com/N7ghtm4r3)

## Support

If you need help using the library or encounter any problems or bugs, please contact us via the
following links:

- Support via <a href="mailto:infotecknobitcompany@gmail.com">email</a>
- Support via <a href="https://github.com/N7ghtm4r3/Ametista-Engine/issues/new">GitHub</a>

Thank you for your help!

## Badges

[![](https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white)](https://play.google.com/store/apps/developer?id=Tecknobit)

## Donations

If you want support project and developer

| Crypto                                                                                              | Address                                          | Network  |
|-----------------------------------------------------------------------------------------------------|--------------------------------------------------|----------|
| ![](https://img.shields.io/badge/Bitcoin-000000?style=for-the-badge&logo=bitcoin&logoColor=white)   | **3H3jyCzcRmnxroHthuXh22GXXSmizin2yp**           | Bitcoin  |
| ![](https://img.shields.io/badge/Ethereum-3C3C3D?style=for-the-badge&logo=Ethereum&logoColor=white) | **0x1b45bc41efeb3ed655b078f95086f25fc83345c4**   | Ethereum |
| ![](https://img.shields.io/badge/Solana-000?style=for-the-badge&logo=Solana&logoColor=9945FF)       | **AtPjUnxYFHw3a6Si9HinQtyPTqsdbfdKX3dJ1xiDjbrL** | Solan    |

If you want support project and developer
with <a href="https://www.paypal.com/donate/?hosted_button_id=5QMN5UQH7LDT4">PayPal</a>

Copyright © 2025 Tecknobit
