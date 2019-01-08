# uniLive

Vor dem Starten: (http://www.programmierenlernenhq.de/tutorial-android-app-installieren/)
- Entwickleroptionen im Handy aktivieren:
    - Einstellungen -> System -> Über das Telefon -> Build-Nummer 7 mal antippen
    - In den Entwickleroptionen "USB-Debugging" und "wach bleiben" aktivieren
- USB-Treiber müssen nicht extra installiert werden, da sie im Android Studio schon enthalten sind (https://developer.android.com/studio/run/win-usb)?
- Einstellungen im Handy ändern:
    - Einstellungen -> Apps & Benachrichtigungen -> App-Berechtigungen -> Mikrofon und Kamera für die eigene Android App aktivieren,
    damit die Frequenzanzeige funktioniert und die App nicht abstürzt


In der Gyroscope.java liegt der Beispielcode
In der Acceleration.java liegt der Code, den die Schüler ergänzen sollen
-> beide Klassen greifen auf die gleiche Activity zu

Quellen:
- Kameralicht: https://stackoverflow.com/questions/6068803/how-to-turn-on-front-flash-light-programmatically-in-android
- Vibration: https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
- Sound: https://abhiandroid.com/androidstudio/add-audio-android-studio.html
- Frequenzanzeige: https://0110.be/tags/TarsosDSP
- Klatschen erkennen: https://stackoverflow.com/questions/36971839/tarsosdsp-clap-detection