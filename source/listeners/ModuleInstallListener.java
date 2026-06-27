package presidential.Green.Durham.Grass.and.Herb.source.listeners;

import java.io.*;
import java.net.*;
import java.sql.*;

/**
 * Module Install Listener — Port 2000
 *
 * Accepts jar files (with Installer ID from Max Rupplin - MEARVK LLC)
 * and sql files (with similar Installer ID).
 *
 * Requirements for acceptance:
 * - Valid Installer ID from MEARVK LLC (Max Rupplin)
 * - National ID with Moral Rating of "Very Good" or better
 * - IQ over 125
 */
public class ModuleInstallListener {

    private static final int PORT = 2000;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/green_durham_grass_and_herb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final String INSTALLER_AUTHORITY = "MEARVK LLC";
    private static final int MIN_IQ = 126; // "over 125"

    // Moral ratings in ascending order
    private static final String[] MORAL_RATINGS = {
        "Very Poor", "Poor", "Below Average", "Average",
        "Above Average", "Good", "Very Good", "Excellent", "Outstanding"
    };
    private static final int MIN_MORAL_INDEX = 6; // "Very Good" index

    public static void main(String... args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("[MODULE-INSTALL] Listening on port " + PORT);
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handle(client)).start();
            }
        } catch (Exception e) {
            throw new RuntimeException("Module install listener failed on port " + PORT, e);
        }
    }

    private static void handle(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String ip = client.getInetAddress().getHostAddress();

            // Step 1: Request Installer ID
            out.println("MODULE INSTALL SERVER (Port 2000) — Provide InstallerID:");
            String installerId = in.readLine();
            if (installerId == null || !installerId.contains(INSTALLER_AUTHORITY)) {
                out.println("REJECTED: Installer ID must be from " + INSTALLER_AUTHORITY + " (Max Rupplin).");
                return;
            }

            // Step 2: Request National ID
            out.println("Provide NationalID:");
            String nationalId = in.readLine();
            if (nationalId == null || nationalId.isBlank()) {
                out.println("REJECTED: NationalID required.");
                return;
            }

            // Step 3: Request Moral Rating
            out.println("Provide MoralRating:");
            String moralRating = in.readLine();
            if (!isAcceptableMoralRating(moralRating)) {
                out.println("REJECTED: Moral Rating must be Very Good or better.");
                return;
            }

            // Step 4: Request IQ
            out.println("Provide IQ:");
            String iqStr = in.readLine();
            int iq;
            try {
                iq = Integer.parseInt(iqStr != null ? iqStr.trim() : "0");
            } catch (NumberFormatException e) {
                iq = 0;
            }
            if (iq < MIN_IQ) {
                out.println("REJECTED: IQ must be over 125.");
                return;
            }

            // Step 5: Request file type and name
            out.println("Provide FileType (jar/sql):");
            String fileType = in.readLine();
            if (fileType == null || (!fileType.equalsIgnoreCase("jar") && !fileType.equalsIgnoreCase("sql"))) {
                out.println("REJECTED: Only jar and sql files accepted.");
                return;
            }

            out.println("Provide FileName:");
            String fileName = in.readLine();
            if (fileName == null || fileName.isBlank()) {
                out.println("REJECTED: FileName required.");
                return;
            }

            // Step 6: Accept and receive file bytes
            out.println("ACCEPTED: Send file size (bytes) then file data.");
            String sizeStr = in.readLine();
            int fileSize;
            try {
                fileSize = Integer.parseInt(sizeStr != null ? sizeStr.trim() : "0");
            } catch (NumberFormatException e) {
                out.println("ERROR: Invalid file size.");
                return;
            }

            // Read raw bytes for the file
            InputStream rawIn = client.getInputStream();
            byte[] fileData = rawIn.readNBytes(fileSize);

            // Save to appropriate directory
            String destDir = fileType.equalsIgnoreCase("jar") ? "jars" : "install";
            File destFile = new File(destDir + File.separator + fileName);
            destFile.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(destFile)) {
                fos.write(fileData);
            }

            // Log the install
            storeInstall(installerId, nationalId, moralRating, iq, fileType, fileName, ip);

            out.println("SUCCESS: Module '" + fileName + "' installed to " + destDir + "/");
            System.out.println("[MODULE-INSTALL] Installed " + fileName + " from " + ip + " (ID: " + installerId + ")");

        } catch (Exception e) {
            System.err.println("[MODULE-INSTALL] Client error: " + e.getMessage());
        }
    }

    private static boolean isAcceptableMoralRating(String rating) {
        if (rating == null || rating.isBlank()) return false;
        String trimmed = rating.trim();
        for (int i = MIN_MORAL_INDEX; i < MORAL_RATINGS.length; i++) {
            if (MORAL_RATINGS[i].equalsIgnoreCase(trimmed)) return true;
        }
        return false;
    }

    private static void storeInstall(String installerId, String nationalId, String moralRating, int iq, String fileType, String fileName, String ip) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO module_installs (installer_id, national_id, moral_rating, iq, file_type, file_name, ip, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())")) {
            ps.setString(1, installerId);
            ps.setString(2, nationalId);
            ps.setString(3, moralRating);
            ps.setInt(4, iq);
            ps.setString(5, fileType);
            ps.setString(6, fileName);
            ps.setString(7, ip);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[MODULE-INSTALL] Store failed: " + e.getMessage());
        }
    }
}
