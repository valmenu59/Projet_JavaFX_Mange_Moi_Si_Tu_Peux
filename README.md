#titre Projet JavaFX "Mange-moi si tu peux !"

##titre Le but du projet était de créer un mini jeu où un loup et un mouton sont dans un enclos. L'objectif du loup est de manger le mouton et l'objectif du mouton est manger le plus de plantes possibles au sein de l'enclos.

Avant de lancer le jeu, l'utilisateur doit passer par plusieurs étapes afin de créer le terrain : 
  * Il peut choisir le nombre de lignes et de colonnes selon les possiblités prédifinis
  * Il peut choisir la case de sortie
  * Il peut choisir de générer un labyrinthe aléatoire et également de choisir le type de terrain pour une case en particulier (une roche, une herbe, un cactus ou une marguerite)
  * Il peut choisir l'emplacement de départ du loup et du mouton (par contre il ne peut pas placer les animaux sur une case roche)
  * Il peut choisir de sauvegarder l'état du plateau ou non (la sauvegarde se fait via un fichier binaire)

Lors de la simulation, les animaux se déplacent un par un jusqu'à temps que le nombre de déplacement restant soit égal à 0 :
  * Par défaut le mouton avance de 2 cases. A la fin de ses déplacements, il mange une plante (s'il en a) :
    * S'il mange une herbe, il avance de 2 cases. S'il est menacé par le loup, il y a 80% qu'il avance de 3 cases, 1% qu'il avance de 4.
    * S'il mange un cactus, il n'avance que d'une case. S'il est menacé par le loup, il y a 60% de chance qu'il avance de 2 cases, 15% de 3.
    * S'il mange une marguerite, il avance de 4 cases. S'il est menacé par le mouton, il y a 10% de chance qu'il avance de 5 cases.
  * Le loup avance toujours de 3 cases peut importe la situation

Le mouton est menacé par le loup seulement si la distance de Manhanttan est inférieure ou égale à 5 ET qu'il n'y a pas de roche qui obstrue la vision du loup vers le mouton.

A chaque déplacement des animaux, ils laissent chacun des phéromones qui restent pendant x tours. (la durée de vie en nombre de tours est proportionnelle à taille du plateau divisé par 12).
En cas de non-menace le déplacement des animaux se fait aléatoirement sauf que les phéromones ont un influence sur le déplacement :
  * Le loup est plus attiré par les cases où il y a des phéromones mouton
  * Le mouton est moins attiré par les cases où il y a des phéromones loup
  * Les animaux sont moins attirés par les cases où il y a déjà ses propres phéromones, ils ont plus tendance à aller sur les cases sans phéromones.

Par contre en cas de menace :
  * Le mouton se dérige vers la case de sortie.
  * Le loup se dirige vers le mouton

Le jeu se termine lorsque le mouton est arrivé sur la case de sortie (victoire) ou que le loup a mangé le mouton (défaite).

Pendant que le jeu tourne, l'utilisateur peut : 
  * Changer la vitesse de déplacement des animaux
  * Choisir l'algorithme de recherche lorsque le mouton est menacé selon les choix possibles.

-----------------------------------------------------------------

En dehors du jeu, l'utilisateur peut en plus : 
  * Ouvrir le dernier plateau sauvegardé
  * Ouvrir un des 8 plateaux préconfigurés
  * Ouvrir un plateau via un explorateur de fichiers :
    * Il peut ouvrir un fichier binaire d'extension .sae
    * Il peut ouvrir un fichier texte de format .txt

Pour le fichier texte voici un exemple : 

[Lien d'un exemple d'un fichier texte](https://1drv.ms/t/s!Av-iByuQhqOsrcoVoy0lrTdK4rmRbA?e=a9RyQS)


x : Une roche

f : Une marguerite 

h ou s : Une herbe

c : Un cactus

m : Un mouton

l : Un loup

Il est tout à fait possible de mettre les lettres en majuscules.

Si une des conditions du création du plateau n'est pas remplie, une erreur indicative sera affichée.


-------------------------------------------------------------------------


Voici les différents algorithmes utilisés : 
   * Pour la vérification de labyrinthe parfait : Utilisation du parcours en profondeur
   * Pour la vérifier la distance de Manhanttan et pour donner des poids aux cases : Utilisateur du parcours en largeur
   * Pour vérifier l'obstruction de vision du loup vers le mouton : Utilisation de l'algorithme de tracé de segment de Bresenham
   * Pour le déplacement du loup et du mouton : Utilisation de l'algorithme de Dijkstra et de l'algorithme A* (peut-être modifié selon le choix de l'utilisateur)
   * Pour le mouton, il y a un algorthme A* amélioré où plusieurs conditions sont ajoutées afin d'améliorer ses chances de survie.
