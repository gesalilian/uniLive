# uniLive

Vor dem Starten: (http://www.programmierenlernenhq.de/tutorial-android-app-installieren/)
- Entwickleroptionen im Handy aktivieren:
    - Einstellungen -> System -> Über das Telefon -> Build-Nummer 7 mal antippen
    - In den Entwickleroptionen "USB-Debugging" und "wach bleiben" aktivieren
- USB-Treiber müssen nicht extra installiert werden, da sie im Android Studio schon enthalten sind (https://developer.android.com/studio/run/win-usb)?
- Einstellungen im Handy ändern:
    - Einstellungen -> Apps & Benachrichtigungen -> App-Berechtigungen -> Mikrofon, Speicher und Kamera für die eigene Android App aktivieren,
    damit die Frequenzanzeige funktioniert und die App nicht abstürzt

- um die externe Bibliothek für die Frequenzanzeige zu importieren:
    -> im Android Studio oben links auf Android-> Project -> app -> libs klicken, rechtsklick auf die Jar-Datei -> Add as library -> OK
    (evtl. Build -> Rebuild project klicken, um das Projekt zu erneuern)

Anmerkungen:
- In der Gyroscope.java liegt der Beispielcode
- In der Acceleration.java liegt der Code, den die Schüler ergänzen sollen


Quellen:
- Kameralicht: https://stackoverflow.com/questions/6068803/how-to-turn-on-front-flash-light-programmatically-in-android
- Vibration: https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
- Sound: https://abhiandroid.com/androidstudio/add-audio-android-studio.html
- Frequenzanzeige: https://0110.be/tags/TarsosDSP
- Klatschen erkennen: https://stackoverflow.com/questions/36971839/tarsosdsp-clap-detection

TODO: 1 Bisher kann die Frequenz nur angezeigt werden, solange man im "Beispiel" bleibt. Wenn man zurück ins Hauptmenü kommt und erneut auf "Beispiel" klickt, ist die Frequenzanzeige verschwunden
TODO: 2 Wenn man aus dem "Beispiel" wieder herauskommt, (manchmal auch erst, wenn man erneut auf "Beispiel" klickt), erscheinen in der Debugger-Console zwei Anmerkungen, die ich nicht beheben konnte.
1. "E/AudioRecord: start() status -38" -> weil die Threads, die bei der frequence-Funktion gestartet werden nicht beendet werden?
2. "W/MediaPlayer-JNI: MediaPlayer finalized without being released" -> weil die Sounds nicht immer beendet werden, sondern überspielt, weil die "onSensorChanged"-Funktion schneller als der Sound ist?
3. Weitere Anmerkungen, die ich nicht einordnen konnte