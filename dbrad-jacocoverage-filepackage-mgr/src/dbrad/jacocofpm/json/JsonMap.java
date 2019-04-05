/* Copyright (c) 2017 dbradley. All rights reserved.
 */
package dbrad.jacocofpm.json;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dbrad.jacocofpm.config.IdeProjectJacocoverageConfig;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;

/**
 * Class holder the JSON data for a project's settings for all its preference,
 * project-specific settings.
 * <pre>
 * JsonMap
 * 1    K: General preferences
 *   1      "project-specific"        data:
 *   1        ::
 *   1        ::
 *
 * 1   K: Excludes-filter
 *  N+    exclude-package-filter-string1  K:
 *   1      onoroff                       E: data:
 *
 * 1   K: Package-filter-tables
 *  N+   Json1ProjLevel               K: project name
 *   N+    Json2ProjSrcCodeFolder      K: source-code-folder name (scr/test)
 *    N+     Json3PackageFormat         K: package-format string (pck1_1.pck1_2)
 *     1       Json4Data                 E:
 *                "on"                    data:
 *                "isTst"                 data:
 *                "hasJava"               data:
 *                "pfCvr"                 data:
 *
 * N+ multiple keys of      K: key     E: element    data: is data item
 * 1  one-of
 * </pre>
 *
 * @author dbradley
 */
public class JsonMap {

    private static final Logger LOGGER = Logger.getLogger(JsonMap.class.getName());

    /** key for the general and project-specific preferences */
    public static final String JSON_GENERAL = "preferences";

    /** key for the package-filter tables */
    public static final String JSON_PKGFILTER = "pkgfltr.listing";

    /** key for the exclude-packages table for a project */
    public static final String JSON_EXCLUDE_PACKAGES = "exclpkgs.listing";

    /** JSon mapper variable. */
    private final ObjectMapper mapper = new ObjectMapper();

    /** the Ide project configuration object for this projects
     * dbrad-jacocoverage */
    private final IdeProjectJacocoverageConfig ideProjConfig;

    /** file used for JSON data configuration and persistence. */
    private File jsonDataFile;

    /**
     * The map of all the JSON data relating to a projects configuration
     * preferences, package-filter, excludes and additional packages.
     */
    private final Map<String, Object> pref = new HashMap<>(4);

    /**
     * Get the preferences properties from the JSON data.
     *
     * @return Properties (Java Properties) object
     */
    public Properties getJsonPreferences() {
        return (Properties) pref.get(JSON_GENERAL);
    }

    /**
     * Get the project and depending-on (Netbeans sub-projects) projects
     * package-filtering setting from the Json data.
     *
     * @return Json1ProjLevel object
     */
    @SuppressWarnings("unchecked")
    public Json1ProjLevel getProjectIncludePackages() {
        // the data is
        // key1: sourceFolder directory name (is unique for the project as either 'src' or 'test'
        // key2: the package-string ("" is no package string or the top of the source-code)
        // element: boolean indicating true is test-code vs false src-code directory
        Json1ProjLevel pkgFilter = (Json1ProjLevel) pref.get(JSON_PKGFILTER);
        return pkgFilter;
    }

    /**
     * Get the exclude package data structure from the JSON map data (either the
     * file or internal variable as appropriate).
     *
     * @return HashMap of the exclude-packages structure
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, Boolean> getExcludePackages() {

        HashMap<String, Boolean> excludePackagesArr = (HashMap< String, Boolean>) pref.get(JSON_EXCLUDE_PACKAGES);

        return excludePackagesArr == null ? new HashMap<>() : excludePackagesArr;
    }

    // - - - - - - - - - store - - - - - - - 
    /**
     * Store project's configuration.
     *
     * @throws IOException if cannot store configuration.
     */
    public void store()
            throws IOException {
        jsonDataFile.getParentFile().mkdirs();

        if (jsonDataFile.exists() && !jsonDataFile.delete()) {
            throw new IOException("Cannot write project's jacocoverage config to: " + jsonDataFile);
        } else {
            mapper.writeValue(jsonDataFile, pref);
        }
    }

    // - - - - - - - - - load -- - - - - -  -
    /**
     * Load project's configuration.
     */
    @SuppressWarnings("unchecked")
    public void load() {
        // reload the data from the JSON file, to do this get the
        // internal collections storage and clear() the settings, then load into
        // those collections (HashMap, ArrayList,......).

        jsonDataFile.getParentFile().mkdirs();

        boolean loadedJson = false;

        if (jsonDataFile.exists()) {
            try {
                // ------- preferences -----------
                getJsonPreferences().clear();

                Map<Object, Object> preferences = (Map<Object, Object>) mapper.readValue(jsonDataFile, Map.class).get(JSON_GENERAL);
                getJsonPreferences().putAll(preferences);

                // ------- include packages ----------- 
                getProjectIncludePackages().clear();
                //
                HashMap<String, // key1: project-name
                        HashMap<String, // key2: source-code folder name
                        HashMap<String, // key3: package-format   ele3: data for package-format
                        HashMap<String, Object>>>> // key4: the data item keys of Json4Data
                        projectHashMap = (HashMap<String, HashMap<String, HashMap<String, HashMap<String, Object>>>>) mapper.readValue(jsonDataFile, Map.class).get(JSON_PKGFILTER);

                Json1ProjLevel convertedHash = getProjectIncludePackages();

                projectHashMap.forEach((projectNameKey, hashMapSrcCodeValue) -> {
                    hashMapSrcCodeValue.forEach((srcCodeFolderNameKey, hashMapPkgFormatValue) -> {
                        hashMapPkgFormatValue.forEach((packageFormatKey, hashMap4Data) -> {

                            // allocate the storage for the cascade of HashMap
                            // and to store the data for the Map
                            //
                            // as the keys may have sibling data, only allocate as necessary
                            if (!convertedHash.containsKey(projectNameKey)) {
                                // create the storage
                                convertedHash.put(projectNameKey, new Json2ProjSrcCodeFolder(4));
                            }
                            Json2ProjSrcCodeFolder projSrcHash = convertedHash.get(projectNameKey);
                            // ----
                            if (!projSrcHash.containsKey(srcCodeFolderNameKey)) {
                                // create the storage
                                projSrcHash.put(srcCodeFolderNameKey, new Json3PackageFormat(4));
                            }
                            Json3PackageFormat pkgFormatHash = projSrcHash.get(srcCodeFolderNameKey);
                            // ----
                            Json4Data jsonDataHash = pkgFormatHash.put(packageFormatKey,
                                    Json4Data.createFromHash(hashMap4Data));
                        });
                    });
                });
                // ------- exclude packages -----------
                getExcludePackages().clear();

                HashMap<String, Boolean> excludePackages
                        = (HashMap<String, Boolean>) mapper.readValue(jsonDataFile, Map.class).get(JSON_EXCLUDE_PACKAGES);

                if (excludePackages != null) {
                    // using an older form of input file so carry on and the file will be reset at some point in time
                    getExcludePackages().putAll(excludePackages);
                }
                // - - - - - - 
                loadedJson = true;

            } catch (JsonMappingException ex) {
                loadedJson = false;
                LOGGER.log(Level.INFO, "Project's JaCoCoverage configuration file JSON format is outdated or invalid. Reset cause:", ex);
            } catch (IOException ex) {
                loadedJson = false;
                LOGGER.log(Level.INFO, "Project's JaCoCoverage configuration file format is outdated or invalid. Reset cause:", ex);
            }
        }
        // if the JSON was loaded okay there is nothing more to do
        if (loadedJson) {
            return;
        }
        String msg = "The project's JaCoCoverage configuration file format is required/outdated/invalid.\n"
                + "The configuration file has been reset/created.";
        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(msg, NotifyDescriptor.WARNING_MESSAGE));

        // create a shell file as the new file
        try {
            getJsonPreferences();

            this.store();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Create a JSON map structure and file for the IDE project configuration
     * provided.
     *
     * @param ideProjConfig an IDE principle-project for dbrad-jacocoverage
     */
    public JsonMap(IdeProjectJacocoverageConfig ideProjConfig) {
        this.ideProjConfig = ideProjConfig;

        initialize();
    }

    private void initialize() {
        this.jsonDataFile = this.ideProjConfig.getProjectJsonFile();

        pref.put(JSON_GENERAL, new Properties());
        pref.put(JSON_PKGFILTER, new Json1ProjLevel(16));
        pref.put(JSON_EXCLUDE_PACKAGES, new HashMap<>(16));

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        mapper.enable(SerializationFeature.EAGER_SERIALIZER_FETCH);
    }
}
