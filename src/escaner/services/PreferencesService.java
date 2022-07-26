package escaner.services;

import java.util.prefs.Preferences;

public class PreferencesService {
    private static final String FILE_KEY = "file_key";
    public static final String FILE_DEFAULT_VALUE = "diccionario.sc";

    public String GetDefultFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(escaner.Escaner.class);
		return prefs.get(FILE_KEY, FILE_DEFAULT_VALUE);
    }

    public void SetDefaultFilePath(String file) {
        Preferences prefs = Preferences.userNodeForPackage(escaner.Escaner.class);
        prefs.put(FILE_KEY, file);
    }
}
