# Squawker-App
This is the app that I made following the Udacity [Firebase Cloud Messaging Lesson](https://classroom.udacity.com/courses/ud855#). This app shows you `Sqawks` from the udacity instructors. There is also an option to select whose Squawk you want to see and notified for.

The app use Firebase Cloud messaging to send notification to the user or to get squawks from different instructors.

## How to use the app -
  1. First you need a [Firebase Account](https://firebase.google.com/), after this you need to head to the `Console` and then register the app. During the process you will a get a `google-services.json` file. You need to place it in the app folder of the project.
  2. Then you need to open [this server](https://squawkerfcmserver.udacity.com/), to send squawks. When you fisrt run the app in the logcat, you will see a `Instance Token`, copy it and paste in the `ID Token` EditText.
  3. Then open the settings of your project in the firebase console and got to the `Cloud Messaging` tab. You will see `Server Key` copy it and paste it in the Server Key EditText of the server site.
  4. Lastly click on `Send Squawk` and you will recive a notification and the message will the displayed in the app.
