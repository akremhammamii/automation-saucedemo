# language: fr

Fonctionnalité: Connexion des utilisateurs au site Sauce Demo

  En tant qu'utilisateur du site web
  Je souhaite me connecter au site
  Afin d'accéder à mon compte et à la liste des produits

  @test
  Plan du Scénario: Tester les différents cas de connexion utilisateur

    # Les étapes utilisent des variables entre chevrons (<...>)
    Etant donné Je visite le site saucedemo
    Quand Je saisis username "<nom_utilisateur>"
    Et Je saisis mot de passe "<mot_de_passe>"
    Et Je clique sur le bouton login
    Alors le statut de la connexion est "<statut>" avec le message "<message_attendu>"

    Exemples:
      | nom_utilisateur           | mot_de_passe  | statut | message_attendu                                     |
      | standard_user             | secret_sauce  | succès | Accès au catalogue (réussi)                         |
      | locked_out_user           | secret_sauce  | échec  | Epic sadface: Sorry, this user has been locked out. |
      | problem_user              | secret_sauce  | succès | Accès au catalogue (images cassées)                 |
      | performance_glitch_user   | secret_sauce  | succès | Accès au catalogue (lent)                           |