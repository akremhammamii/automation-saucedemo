package com.saucedemo.utils;

import org.openqa.selenium.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Imports pour la gestion des fichiers et du temps
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    // Dossier où seront sauvegardées les captures d'écran
    private static final String SCREENSHOT_DIR = "target/screenshots";

    /**
     * Effectue une capture d'écran de la page entière et la sauvegarde dans un fichier.
     * Le nom du fichier inclut l'heure pour garantir son unicité.
     *
     * @param driver L'instance de WebDriver.
     * @param scenarioName Le nom du scénario ou du contexte de la capture.
     * @return Le chemin d'accès absolu au fichier sauvegardé, ou null en cas d'échec.
     */
    public static String takeScreenshot(WebDriver driver, String scenarioName) {
        if (!(driver instanceof TakesScreenshot)) {
            System.err.println("Le driver ne supporte pas la capture d'écran.");
            return null;
        }

        // 1. Prendre la capture d'écran
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // 2. Préparer le nom et le chemin du fichier
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = now.format(formatter);

        // Nettoyer le nom du scénario pour le rendre compatible avec les noms de fichiers
        String cleanScenarioName = scenarioName.replaceAll("[^a-zA-Z0-9_\\-]", "_");

        String fileName = String.format("%s_%s.png", cleanScenarioName, timestamp);
        Path targetPath = Paths.get(SCREENSHOT_DIR, fileName);

        try {
            // Assurez-vous que le dossier existe
            Files.createDirectories(targetPath.getParent());

            // 3. Copier le fichier temporaire vers le chemin cible
            Files.copy(screenshotFile.toPath(), targetPath);

            String absolutePath = targetPath.toAbsolutePath().toString();
            System.out.println("Screenshot sauvegardée : " + absolutePath);

            return absolutePath;

        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de la capture d'écran : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Prend une capture d'écran de la page entière, dessine un rectangle rouge 
     * autour de l'élément spécifié, et sauvegarde le résultat.
     * 
     * Cette méthode utilise la convention de nommage de takeScreenshot.
     * 
     * @param driver Le WebDriver actif.
     * @param element L'élément WebElement à mettre en évidence.
     * @param scenarioName Le nom du scénario ou du contexte de la capture.
     * @return Le chemin d'accès absolu au fichier sauvegardé, ou null en cas d'échec.
     */
    public static String takeAnnotatedScreenshot(WebDriver driver, WebElement element, String scenarioName) {
        
        // 1. Préparation du chemin de sortie (même logique que takeScreenshot)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = now.format(formatter);
        String cleanScenarioName = scenarioName.replaceAll("[^a-zA-Z0-9_\\-]", "_") + "_ANNOTATED";
        String fileName = String.format("%s_%s.png", cleanScenarioName, timestamp);
        
        Path targetPath = Paths.get(SCREENSHOT_DIR, fileName);
        
        try {
            // Assurez-vous que le dossier existe
            Files.createDirectories(targetPath.getParent());

         // 2. Récupérer les coordonnées de l'élément
            // Utilisation des noms de classes entièrement qualifiés pour les types Selenium
            org.openqa.selenium.Point location = element.getLocation();
            org.openqa.selenium.Dimension size = element.getSize();
            
            int x = location.getX();
            int y = location.getY();
            int width = size.getWidth();
            int height = size.getHeight();
            
            // 3. Prendre la capture d'écran brute
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // 4. Charger l'image et préparer le dessin
            BufferedImage image = ImageIO.read(screenshotFile);
            Graphics2D g2d = image.createGraphics();

            // Paramètres du dessin
            g2d.setColor(Color.RED); 
            g2d.setStroke(new BasicStroke(5)); 

            // 5. Dessiner le rectangle
            g2d.drawRect(x, y, width, height);
            g2d.dispose();

            // 6. Sauvegarder l'image modifiée
            File finalScreenshot = targetPath.toFile();
            ImageIO.write(image, "png", finalScreenshot);
            
            String absolutePath = finalScreenshot.getAbsolutePath();
            System.out.println("Capture d'écran annotée enregistrée à : " + absolutePath);
            return absolutePath;
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la manipulation ou de la sauvegarde de l'image annotée : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}