package com.saucedemo.stepdefinitions;

import com.saucedemo.context.ScenarioContext;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import io.cucumber.java.fr.Alors;
import org.junit.jupiter.api.Assertions;

public class LoginStepDefinitions {

    private final ScenarioContext context;

    public LoginStepDefinitions(ScenarioContext context) {
        this.context = context;
    }

    @Etantdonné("Je visite le site saucedemo")
    public void jeVisiteLeSiteSaucedemo() {
        String url = context.configFileReader.getProperties("home.url");
        context.seleniumUtils.navigateTo(url);
    }

    @Quand("Je saisis username {string}")
    public void jeSaisisUsername(String username) {
        context.loginPage.enterUsername(username);
    }

    @Et("Je saisis mot de passe {string}")
    public void jeSaisisMotDePasse(String password) {
        context.loginPage.enterPassword(password);
    }

    @Et("Je clique sur le bouton login")
    public void jeCliqueSurLeBoutonLogin() {
        context.loginPage.clickLogin();
    }

    @Alors("le statut de la connexion est {string} avec le message {string}")
    public void verifierConnexion(String statut, String messageAttendu) {
        if (statut.equalsIgnoreCase("succès")) {
            // ✅ Exemple : vérifier que l’URL contient /inventory.html
        	String currentUrl = context.getDriver().getCurrentUrl();


        	Assertions.assertTrue(currentUrl.contains("/inventory"),
        		    "L'utilisateur ne s'est pas redirigé vers le catalogue !");

        } else {
        	Assertions.assertTrue(context.loginPage.isErrorDisplayed(),
        		    "Aucun message d'erreur affiché !");
        		Assertions.assertEquals(messageAttendu,
        		    context.loginPage.getErrorMessage(),
        		    "Message d'erreur incorrect !");

        }
    }
}
