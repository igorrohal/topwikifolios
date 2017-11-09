package eu.irohal.topwikifolios.clients;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

public class GDriveClient {

/**
 * Be sure to specify the name of your application. If the application name is {@code null} or
 * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
 */
private static final String APPLICATION_NAME = "Topwikifolios/1.0";

private static final String UPLOAD_FILE_PATH = "./sample_csv/topwikifolios.csv";
private static final java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);

/** Directory to store user credentials. */
private static final java.io.File DATA_STORE_DIR = new java.io.File("./.store");

/**
 * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
 * globally shared instance across your application.
 */
private static FileDataStoreFactory dataStoreFactory;

/** Global instance of the HTTP transport. */
private static HttpTransport httpTransport;

/** Global instance of the JSON factory. */
private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

/** Global Drive API client. */
private static Drive drive;

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(GDriveClient.class.getResourceAsStream("/client_credentials.json")));
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory)
                .build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static void main(String[] args) {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up the global Drive instance
            drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
                    APPLICATION_NAME).build();

            // run commands
            uploadFile();

            return;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }

    /** Uploads a file using either resumable or direct media upload. */
    private static File uploadFile() throws IOException {
        String folderId = findFolderId();
        FileList fileList = drive.files().list().execute();

        File fileMetadata = new File();
        fileMetadata.setTitle("uploaded_sample_by_app.csv");
        ParentReference folder = new ParentReference();
        folder.setId("TopWikifolios");
        fileMetadata.setParents(Collections.singletonList(folder));

        FileContent mediaContent = new FileContent("text/csv", UPLOAD_FILE);

        Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
        MediaHttpUploader uploader = insert.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(true);
        return insert.execute();
    }

    private static String findFolderId() {
        File fileMetadata = new File();
        fileMetadata.setTitle("TopRankedWikifolios");
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

//        File file = drive.files().insert(fileMetadata)
//                .setFields("id")
//                .execute();
//        return file.getId();
        return null;
    }

}