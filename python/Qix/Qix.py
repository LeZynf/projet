

#--------------------------------------------------------------------------------
#                               Imports
from fltk import *
from time import *
import random
from random import randint
from time import time
#--------------------------------------------------------------------------------

cree_fenetre (800,800)


zone = [(10,90),(10,790),(790,790),(790,90)]

def zonejeu(x): 
    polygone(x, epaisseur = 5 ,couleur = "red" ,tag = 'zone' )


#définitions de l'événement de défaite
def perdu():
    efface_tout()
    texte(
        400, 400,
        "GAME OVER", "red", "center"
    )
    attend_ev()


#--------------------------------------------------------------------------------
#                               Losange de départ
#--------------------------------------------------------------------------------

p1,q1, p2,q2, p3,q3, p4,q4 = 400,800,  410,790,  400,780,  390,790  #point pour le losange (personnage)
sommet = [p1,q1 , p2,q2, p3,q3 , p4,q4]
tracage= []
tracage2= []


avant = "bord"
avant2 = "bord"

oldzone = 0
newzone = 0
def personage(x): 
    polygone(x, epaisseur = 4 ,couleur = "white" ,tag = 'perso' )

def personage2(x): 
    polygone(x, epaisseur = 4 ,couleur = "green" ,tag = 'perso2' )

def terrain (p) : 
    polygone(p,remplissage ="purple",couleur = "blue", epaisseur = 5 , tag = "Poly")
polycoordo = []    #liste des coordonnées des sommets des polygones à tracer
polycoordo2d = []
def trait(a ,b) :  #trait qui constitue le chemin
    ligne (a[0],a[1],b[0],b[1],"white", 5,tag = "trait")

def trait2(a ,b) :  #trait qui constitue le chemin
    ligne (a[0],a[1],b[0],b[1],"green", 5,tag = "trait2")

jeu = 1
#vitesse de déplacements
dep = 5

NB=0
NB2=0
fx2 = 0
fy2= 0
pausee = 1

def pause():
    
    while True: 
        texte(400, 400, 'Pause', 'White', ancrage='center',taille = 56, police='Times New Roman', tag='pause')
        mise_a_jour()
        attend_ev()  
        break   


def pris (p) : 
    polygone(p,remplissage ="Purple",couleur = "blue", epaisseur = 5 , tag = "Pris")


def pris2 (p) : 
    polygone(p,remplissage ="yellow",couleur = "blue", epaisseur = 5 , tag = "Pris2")



#si les 3 coordonnées forme un angle, rajoute à polycoordo la 2e coordonnée
def coin_poly(cotéprecedent,cotésuivant) :
    
    if ((cotéprecedent == "Right" or cotéprecedent == "Left") and (cotésuivant == "Up" or cotésuivant == "Down")) or\
       ((cotéprecedent == "Up" or cotéprecedent == "Down") and (cotésuivant == "Right" or cotésuivant == "Left")) :
        polycoordo.append(cordoPerso)
    return polycoordo

def coin_poly2(cotéprecedent,cotésuivant) :
    
    if ((cotéprecedent == "Right" or cotéprecedent == "Left") and (cotésuivant == "Up" or cotésuivant == "Down")) or\
       ((cotéprecedent == "Up" or cotéprecedent == "Down") and (cotésuivant == "Right" or cotésuivant == "Left")) :
        polycoordo2.append(cordoPerso2)
    return polycoordo2

        #--------------------------------------------------------------------------------
        #                                Calcul de l'aire
        #--------------------------------------------------------------------------------

#calcul du pourcentage gagné
def aire_gagnee() :
    aire_totale = aire_rectangle(10, 90, 790, 790)  
    aire_polygone = aire_poly(polycoordo) 

    if aire_totale == 0 :
        return 0  

    pourcentage_gagne = (aire_polygone / aire_totale) * 100
    return pourcentage_gagne


#calcul de l'aire d'un rectangle (Longueur * Largeur)
def aire_rectangle(x1, y1, x2, y2) :
    return abs((x2 - x1) * (y2 - y1))

#calcul de l'aire du polygone
def aire_poly(sommets) :
    aire = 0
    n = len(sommets)

    if n < 2 :
        return 0  

    for k in range(n - 1) :
        x1, y1 = sommets[k]
        x2, y2 = sommets[k + 1]
        aire += (x1 * y2 - x2 * y1)

    dernier_x, dernier_y = sommets[-1]
    premier_x, premier_y = sommets[0]
    aire += (dernier_x * premier_y - premier_x * dernier_y)

    aire = abs(aire) / 2.0
    return aire


#--------------------------------------------------------------------------------
#                               Entre deux points
#--------------------------------------------------------------------------------


def entre2point(coordonnee1, coordonnee2, point):
    x1, y1 = coordonnee1
    x2, y2 = coordonnee2
    x3, y3 = point

    # vérifie si le point est sur la ligne définie par coordonnee1 et coordonnee2
    if point == coordonnee1 or point == coordonnee2:
        return True

    # vérifie si le point est entre coordonnee1 et coordonnee2 le long de l'axe y
    if x1 == x2 and y1 != y2 and x1 == x3:
        y_min, y_max = min(y1, y2), max(y1, y2)
        return y_min < point[1] < y_max

    # vérifie si le point est entre coordonnee1 et coordonnee2 le long de l'axe x
    elif y1 == y2 and x1 != x2 and y1 == y3:
        x_min, x_max = min(x1, x2), max(x1, x2)
        return x_min < point[0] < x_max

    return False



def detect_sideDeb(polycoordo, zone):
    debut = polycoordo[0]
    for i in range(len(zone)-1):
        if entre2point(zone[i],zone[i+1], debut)  or debut == zone[i+1] :
            coinAvantDeb = zone[i]
            return coinAvantDeb

        if entre2point(zone[len(zone)-1], zone[0],debut )  or debut == zone[0]:
            coinAvantDeb = zone[len(zone)-1]
    return coinAvantDeb

def detect_sideFin(polycoordo, zone):
    fin = polycoordo [len(polycoordo)-1]
    for i in range(len(zone)-1):
        if entre2point(zone[i],zone[i+1], fin) or fin == zone[i+1] :
            coinAvantFin = zone[i]
            return coinAvantFin

        if entre2point(zone[len(zone)-1], zone[0],fin) or fin == zone[0]:
            coinAvantFin = zone[len(zone)-1]
    return coinAvantFin


#--------------------------------------------------------------------------------
#                               Poly 1
#--------------------------------------------------------------------------------

def poly1(coinAvantDeb,coinAvantFin, polycoordo):
    coordo1 = []
    deb,fin = 0,0
    for i in range (len(zone)):
        if zone[i] == coinAvantDeb :
            deb = i
        if zone[i] == coinAvantFin :
            fin = i
    if deb > fin:
        coinAvantDeb,coinAvantFin = coinAvantFin,coinAvantDeb
        deb,fin = fin,deb
        polycoordo = list(reversed(polycoordo))
    if deb == 0:
        coordo1.append(zone[0])
        for i in range (len(polycoordo)):
            coordo1.append(polycoordo[i])
        for i in range (fin+1,len(zone)):
            coordo1.append(zone[i])
       
    else:
        for i in range(deb+1):
            coordo1.append(zone[i])
        for i in range (len(polycoordo)):
            coordo1.append(polycoordo[i])
        for i in range (fin+1,len(zone)):
            coordo1.append(zone[i])
    return coordo1


#--------------------------------------------------------------------------------
#                               Poly 2
#--------------------------------------------------------------------------------


def poly2 (coinAvantDeb,coinAvantFin, polycoordo) :
    coordo2 = []
    deb,fin = 0,0
    for i in range (len(zone)):
        if zone[i] == coinAvantDeb :
            deb = i
        if zone[i] == coinAvantFin :
            fin = i
    if deb > fin:

        coinAvantDeb,coinAvantFin = coinAvantFin,coinAvantDeb
        deb,fin = fin,deb
        polycoordo = list(reversed(polycoordo))

    if coinAvantDeb == coinAvantFin :
        return polycoordo
    if deb > fin:
        for i in range (deb+1,fin+1):
            coordo2.append(zone[i])
    else:
        for i in range (fin,deb,-1):
            coordo2.append(zone[i])

      
    
    for i in range(len(polycoordo)):
        coordo2.append(polycoordo[i])
    print ("poly2",coordo2)
    return coordo2



count=0         

cotéprecedent = "Right"
cotésuivent = "Right"

cotéprecedent2 = "Up"
cotésuivent2 = "Up"

polycoordo = [(400,790)]

polycoordo2 = [(790,350)]

aire_totale = aire_poly(zone)

score = 0
score1 = 0
score2 = 0

#--------------------------------------------------------------------------------
#                               Définition Qix
#--------------------------------------------------------------------------------
qx, qy = 400, 405 #coordonnées du qix
speed = 5  #vitesse du qix
changement_direction = 10  #pour empêcher le tremblement

countRepetition = 0

qx2, qy2 = 420, 405 #coordonnées du qix
speed = 5  #vitesse du qix
changement_direction2 = 10  #pour empêcher le tremblement

countRepetition2 = 0


#--------------------------------------------------------------------------------
#                              Qix dans polynome
#--------------------------------------------------------------------------------

def qix_dans_polygone(point, polygone):
    x, y = point
    n = len(polygone)

    # initialise le nombre de tracés traversant le point
    nombre_traces = 0

    # parcours chaque côté du polygone
    for i in range(n):
        x1, y1 = polygone[i]
        x2, y2 = polygone[(i + 1) % n]

        # vérifie si le point est à l'intérieur du segment
        if ((y1 <= y < y2) or (y2 <= y < y1)) and (x < (x2 - x1) * (y - y1) / (y2 - y1) + x1):
            nombre_traces += 1

    # si le nombre de tracés traversant le point est impair, le point est à l'intérieur du polygone
    return nombre_traces % 2 == 1

def point_sur_bordure_polygone(point, polygone):
    x, y = point

    for i in range(len(polygone)):
        x1, y1 = polygone[i]
        x2, y2 = polygone[(i + 1) % len(polygone)]

        # vérifie si le point est sur la bordure du segment
        if (y1 == y2) and (y == y1) and (min(x1, x2) <= x <= max(x1, x2)):
            return True

        if (x1 == x2) and (x == x1) and (min(y1, y2) <= y <= max(y1, y2)):
            return True

    return False



#--------------------------------------------------------------------------------
#                                Définition sparx
#--------------------------------------------------------------------------------

lst_sparx1 = [] #liste à laquelle on ajoute sparx1
lst_sparx2 = [] #liste à laquelle on ajoute sparx2

def sparx1() :
    x_sparx1 = 400 #coordonnée du x 
    y_sparx1 = 90 #coordonnée du y
    speed_sparx = 3 #vitesse du sparx
    direction1 = ["up", "down", "left", "right"] #différentes directions dans lesquelles le sparx peut aller
    return {"x": x_sparx1, "y": y_sparx1, "speed": speed_sparx, "direction1": direction1}

lst_sparx1.append(sparx1())

def sparx2() :
    x_sparx2 = 400 #coordonnée du x 
    y_sparx2 = 90 #coordonnée du y
    speed_sparx = 3 #vitesse du sparx
    direction2 = ["up", "down", "left", "right"] #différentes directions dans lesquelles le sparx peut aller
    return {"x": x_sparx2, "y": y_sparx2, "speed": speed_sparx, "direction2": direction2}
  
lst_sparx2.append(sparx2())


#--------------------------------------------------------------------------------
#                                  Pommes
#--------------------------------------------------------------------------------

#definition des pommes
pommes = []
def pomme() :
    nom_pomme = 'pomme'
    for k in range(randint(3, 5)) :
        x_pomme = randint(10, 790)
        y_pomme = randint(90, 790)
        image(x_pomme, y_pomme, 'pomme.gif', largeur = 40, hauteur = 40, tag = f'{nom_pomme}_{k}')
        pommes.append({'x' : x_pomme, 'y' : y_pomme, 'tag' : f'{nom_pomme}_{k}'})


#collision entre le joueur et la pomme
def manger_pomme(joueur, pomme) :
    x_joueur, y_joueur = joueur[0], joueur[1]
    x_pomme, y_pomme = pomme['x'], pomme['y']
    distance = ((x_joueur - x_pomme) ** 2 + (y_joueur - y_pomme) ** 2) ** 0.5
    return distance < 20

#supprime la pomme une fois mangée
def supprimer_pomme(tag_pomme) :
    efface(tag_pomme)
    for p in pommes :
        if p['tag'] == tag_pomme :
            pommes.remove(p)
            break

mangee = time()


#--------------------------------------------------------------------------------
#                                  Obstacles
#--------------------------------------------------------------------------------

obstacles = []
def creer_obstacles() :
    nom_obstacle = 'obstacle'
    for k in range(1, 4) :
        x_obstacle = randint(200, 650)
        y_obstacle = randint(200, 650)
        epaisseur_obstacle = randint(10, 100)
        rectangle(x_obstacle, y_obstacle, x_obstacle + epaisseur_obstacle, y_obstacle + epaisseur_obstacle, couleur ="pink", epaisseur=epaisseur_obstacle, tag=f'{nom_obstacle}_{k}')
        obstacles.append({'x' : x_obstacle, 'y' : y_obstacle, 'epaisseur' : epaisseur_obstacle, 'tag' : f'{nom_obstacle}_{k}'})


def collision_obstacle(joueur, obstacle):
    x_joueur, y_joueur = joueur[0], joueur[1]
    x_obstacle, y_obstacle = obstacle['x'], obstacle['y']
    epaisseur_obstacle = obstacle['epaisseur']

    collision_x = x_joueur > x_obstacle and x_joueur < x_obstacle + epaisseur_obstacle
    collision_y = y_joueur > y_obstacle and y_joueur < y_obstacle + epaisseur_obstacle

    return collision_x and collision_y



#--------------------------------------------------------------------------------
#                                Interface menu
#--------------------------------------------------------------------------------

def clic ():
    
    ev = attend_ev()
    tev = type_ev(ev)
    if tev == "ClicGauche":
        return abscisse(ev),ordonnee(ev)
    return 0,0


def menu():
    efface_tout()
    rectangle (0,0,800,800, remplissage = "black" )
    image(400,100 , 'QIX.gif', tag='Qix')
    image(400,300 , 'joueur1.gif', tag='Play1')
    image(400,400 , 'joueur2.gif', tag='Play2')
    mise_a_jour()

def vitesse():
    efface_tout()
    rectangle (0,0,800,800, remplissage = "black" )
    image(400,100 , 'QIX.gif', tag='Qix')
    image(200,300 , 'vitesseQ.gif', tag='vitesseQ')
    image(600,300 , 'vitesseQ.gif', tag='vitesseJ')
    image(400,700 , 'play.gif', tag='Play')
    mise_a_jour()

menu()

while True :
    x,y = clic()
    if  300 < x < 500 and 270 < y < 330:
        mode = 'joueur1'
        vitesse()
        while True :
            x,y = clic()
            if 300 < x < 500 and 670 < y < 730:
                break
        
        efface_tout()
        mise_a_jour()
        break
    if  300 < x < 500 and 370 < y < 430:
        mode = 'joueur2'
        vitesse()
        while True :
            x,y = clic()
            if 300 < x < 500 and 670 < y < 730:
                break
        efface_tout()
        mise_a_jour
        break
    
        
if mode == 'joueur1' :
    
    #--------------------------------------------------------------------------------
    #                               Interface jeu
    #--------------------------------------------------------------------------------

    efface_tout()
    rectangle (0,0,800,800, remplissage = "black" )
   
    rectangle(10,90 , 790,790, couleur = "Blue", epaisseur = 5)
    image(400 ,40 , 'GameTime.gif',largeur=300, hauteur=40, tag='GameTime')
    texte(400, 400, 'Appuyez sur Entrée pour jouer', 'White', ancrage='center', police='Times New Roman', tag='intro')
    touche, evenement = attend_ev()
    if touche_pressee("Return") :
        efface('intro')
    pomme()
    creer_obstacles()
    print(obstacles)
    Zoneprise = 0
    texte(2, 0, f'Zone prise : {Zoneprise} %', 'White', police='Times New Roman', tag='zonepris')
    score = 0
    texte(2, 30, f'Score : {abs(score)} ', 'White', police='Times New Roman', tag='Score')
    personage (sommet)

    #--------------------------------------------------------------------------------
    #                               Images vies
    #--------------------------------------------------------------------------------

    #design des vies
    vie = 3
    image(650 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie1')
    image(700 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie2')
    image(750 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie3')



    zonejeu(zone)
    attend_ev()
    while True:
        if touche_pressee("Escape") :
            pause()
        efface ("pause")
        ev = donne_ev()
        tev = type_ev(ev)
    
        if type_ev(ev) == "Quitte":
            break
        cotéprecedent = cotésuivent
        traitdebut = (p1, q2) #debut du trait
        cordoPerso = (p1,q2)
        #print(polycoordo)
        f1,f2,f3,f4,k1,k2,k3,k4 = p1,p2,p3,p4,q1,q2,q3,q4
        if not ((touche_pressee("Left") or touche_pressee("Right")) and (touche_pressee("Up") or touche_pressee("Down")))  :
            if p1 > 10 and touche_pressee("Left"):
                p1,p2,p3,p4 = p1-dep, p2-dep, p3-dep, p4-dep
                cotésuivent = "Left"
            elif p1 < 790 and touche_pressee("Right"):
                p1,p2,p3,p4 = p1+dep,p2+dep,p3+dep,p4+dep
                cotésuivent = "Right" 
            elif q2 < 790 and touche_pressee("Down"):
                q1,q2,q3,q4 = q1+dep,q2+dep,q3+dep,q4+dep
                cotésuivent = "Down"
            elif q2 > 90 and touche_pressee("Up"):
                q1,q2,q3,q4 = q1-dep,q2-dep,q3-dep,q4-dep
                cotésuivent = "Up"
            if not qix_dans_polygone((p1,q2),zone) and not point_sur_bordure_polygone((p1,q2),zone):
                p1,p2,p3,p4,q1,q2,q3,q4=f1,f2,f3,f4,k1,k2,k3,k4
        


            traitfin = (p1, q2)

            if not point_sur_bordure_polygone((p1,q2),zone) :
                if (touche_pressee("Return") and (touche_pressee("Left") or touche_pressee("Right") or touche_pressee("Up") or touche_pressee("Down")))  : 
                    trait(traitdebut,traitfin)   
                else:
                    
                    if p1 >= 10 and touche_pressee("Left"):
                        p1,p2,p3,p4 = p1+dep, p2+dep, p3+dep, p4+dep
                        cotésuivent = cotéprecedent
                        
                    elif p1 <= 790 and touche_pressee("Right"):
                        p1,p2,p3,p4 = p1-dep,p2-dep,p3-dep,p4-dep
                        cotésuivent = cotéprecedent
                    elif q2 <= 790 and touche_pressee("Down"):
                        q1,q2,q3,q4 = q1-dep,q2-dep,q3-dep,q4-dep
                        cotésuivent = cotéprecedent
                    
                    elif q2 >= 90 and touche_pressee("Up"):
                        q1,q2,q3,q4 = q1+dep,q2+dep,q3+dep,q4+dep
                        cotésuivent = cotéprecedent


        coordoQix = (qx,qy)
        coordoQix2 = (qx2,qy2)

        if type_ev(ev) == "Touche":
            coin_poly (cotéprecedent,cotésuivent)
            print (polycoordo)
        
        
            if not point_sur_bordure_polygone((p1,q2),zone):
                avant = "bord"
                tracage.append (traitfin)
            if point_sur_bordure_polygone((p1,q2),zone):
                polycoordo.append(traitfin)
                avant = "zone"

            if point_sur_bordure_polygone((p1,q2),zone) and len(polycoordo) >= 2 and polycoordo[0] != polycoordo[1]  and avant == "zone" and polycoordo != [(400, 790), (400, 785)]:
                print("zone", zone)
                print("polycoordo",polycoordo) 
                coinAvantD = detect_sideDeb(polycoordo,zone)
                print(coinAvantD)
                coinAvantF = detect_sideFin(polycoordo,zone)
                print(coinAvantF)
                zone1 = poly1(coinAvantD,coinAvantF, polycoordo)
                zone2 = poly2(coinAvantD,coinAvantF, polycoordo)
                efface("zone")
                if qix_dans_polygone(coordoQix, zone1):
                    pris (zone2)
                    zonejeu(zone1)
                    zone = zone1
                else:
                    pris (zone1)
                    zonejeu(zone2)
                    zone = zone2
                efface("trait")
                print("zone restante", zone1)
                
                
                print("coin avant deb et fin",coinAvantD, "chef" , coinAvantF)
                print("polycoordo",zone2)
                
                
                zoneair = aire_poly(zone)
                pourcentageZone = round((zoneair / aire_totale) * 100)
                Zoneprise = 100 - pourcentageZone
                NB +=1
                efface ('zonepris')
                texte(2, 0, f'Zone prise : {Zoneprise} %', 'White', police='Times New Roman', tag='zonepris')
                oldzone = Zoneprise
                score += (30-NB) * (newzone - oldzone) * 18 



                efface ('Score')
                texte(2, 30, f'Score : {abs(score)} ', 'White', police='Times New Roman', tag='Score')


                newzone = oldzone 
            if point_sur_bordure_polygone((p1,q2),zone):

                polycoordo = []
                tracage=[]


        #--------------------------------------------------------------------------------
        #                               Mouvement sparx1
        #--------------------------------------------------------------------------------

        for sparx1 in lst_sparx1 :
            #attributs sparx1
            x_sparx1 = sparx1["x"]
            y_sparx1 = sparx1["y"]
            speed_sparx = sparx1["speed"]
            direction1 = sparx1["direction1"]

        #indications permettant au sparx de se déplacer en fonction de sa direction
        if direction1 == "up" :
            y_sparx1 -= speed_sparx
            if y_sparx1 < 90 :  
                direction1 = "right"
        elif direction1 == "down" :
            y_sparx1 += speed_sparx
            if y_sparx1 > 790:  
                direction1 = "left"
        elif direction1 == "left" :
            x_sparx1 -= speed_sparx
            if x_sparx1 < 10 :  
                direction1 = "up"
        else :  
            x_sparx1 += speed_sparx
            if x_sparx1 > 790 :  
                direction1 = "down"

        #limites sparx1 qui servent pour les directions
        x_sparx1 = min(max(x_sparx1, 10), 790)
        y_sparx1 = min(max(y_sparx1, 90), 790)

        #nouveaux attributs
        sparx1["x"] = x_sparx1
        sparx1["y"] = y_sparx1
        sparx1["direction1"] = direction1

        #dessine, supprime le sparx et le fait réapparaître pour le faire "se déplacer"
        efface("sparx1")
        cercle(x_sparx1, y_sparx1, 10, couleur="red", remplissage="red", tag="sparx1")



        #--------------------------------------------------------------------------------
        #                               Mouvement sparx2
        #--------------------------------------------------------------------------------

        for sparx2 in lst_sparx2 :
            #attributs sparx2
            x_sparx2 = sparx2["x"]
            y_sparx2 = sparx2["y"]
            speed_sparx = sparx2["speed"]
            direction2 = sparx2["direction2"]

        #indications permettant au sparx de se déplacer en fonction de sa direction
        if direction2 == "up" :
            y_sparx2 -= speed_sparx
            if y_sparx2 < 90 :  
                direction2 = "left"
        elif direction2 == "down" :
            y_sparx2 += speed_sparx
            if y_sparx2 > 790 : 
                direction2 = "right"
        elif direction2 == "right" :
            x_sparx2 += speed_sparx
            if x_sparx2 > 790 :  
                direction2 = "up"
        else :  
            x_sparx2 -= speed_sparx
            if x_sparx2 < 10 :  
                direction2 = "down"

        
        #limites sparx2 qui servent pour les directions
        x_sparx2 = min(max(x_sparx2, 10), 790)
        y_sparx2 = min(max(y_sparx2, 90), 790)

        #nouveaux attributs
        sparx2["x"] = x_sparx2
        sparx2["y"] = y_sparx2
        sparx2["direction2"] = direction2

        #dessine, supprime le sparx et le fait réapparaître pour le faire "se déplacer"
        efface("sparx2")
        cercle(x_sparx2, y_sparx2, 10, couleur="red", remplissage="red", tag="sparx2")



        #--------------------------------------------------------------------------------
        #                               Mouvement QIX
        #--------------------------------------------------------------------------------

        #if qui compte chaque tour de la boucle et uniformise la vitesse et les coordonnées pour que le qix ne tremble pas et soit fluide
        if countRepetition % changement_direction == 0 :
            fx, fy = random.uniform(-speed, speed), random.uniform(-speed, speed)    
        tx,ty = qx,qy
        qx, qy = qx + fx, qy + fy

        #dessine, supprime le qix et le fait réapparaître le faire "se déplacer"
        efface("Mechant")
        image(qx ,qy , 'Mechant.gif',largeur=70, hauteur=70, tag="Mechant")


        if jeu == 2:
            if countRepetition2 % changement_direction2 == 0 :
                fx2, fy2 = random.uniform(-speed, speed), random.uniform(-speed, speed)    
            tx2,ty2 = qx2,qy2
            qx2, qy2 = qx2 + fx2, qy2 + fy2

            #dessine, supprime le qix et le fait réapparaître le faire "se déplacer"
            efface("Mechant2")
            image(qx2 ,qy2 , 'Mechant.gif',largeur=70, hauteur=70, tag="Mechant2")


        #--------------------------------------------------------------------------------
        #                               Limites pour le qix
        #--------------------------------------------------------------------------------

        #si les coordonnées du qix dépassent ces coordonnées, le qix revient là ce qui le bloque dans le cadre
        if not qix_dans_polygone((qx+35,qy),zone)\
        or not qix_dans_polygone((qx-35,qy),zone)\
        or not qix_dans_polygone((qx,qy+35),zone)\
        or not qix_dans_polygone((qx,qy-35),zone):
            qx,qy = tx,ty
            
        if jeu ==2:
            if not qix_dans_polygone((qx2+35,qy2),zone)\
            or not qix_dans_polygone((qx2-35,qy2),zone)\
            or not qix_dans_polygone((qx2,qy2+35),zone)\
            or not qix_dans_polygone((qx2,qy2-35),zone):
                qx2,qy2 = tx2,ty2
            
        
        #--------------------------------------------------------------------------------
        #           Trait si pas déjà passer par la  / déplacement du perso
        #--------------------------------------------------------------------------------

        sommet = [p1,q1 , p2,q2, p3,q3 , p4,q4] 
        efface('perso')
        personage(sommet)


        #--------------------------------------------------------------------------------
        #                               Collision joueur/obstacle
        #--------------------------------------------------------------------------------

        #si les coordonnées du joueur sont les mêmes que celles de l'obstacle, il revient à ses coordonnées initiales
        for o in obstacles:
            if collision_obstacle([p1, q2], o):
                p1,p2,p3,p4,q1,q2,q3,q4 = f1,f2,f3,f4,k1,k2,k3,k4


        #--------------------------------------------------------------------------------
        #                               Collision joueur/qix
        #--------------------------------------------------------------------------------

        qix_joueur = False

        #si les coordonnées du qix et du joueur sont les mêmes alors il y a collision
        if (p1 <= qx + 35 and p2 >= qx - 35 and q1 <= qy + 35 and q3 >= qy - 35) :
            qix_joueur = True


        if jeu == 2:
            qix2_joueur = False

            #si les coordonnées du qix et du joueur sont les mêmes alors il y a collision
            if (p1 <= qx2 + 35 and p2 >= qx2 - 35 and q1 <= qy2 + 35 and q3 >= qy2 - 35) :
                qix2_joueur = True


        #--------------------------------------------------------------------------------
        #                               Collision qix/chemin
        #--------------------------------------------------------------------------------

        qix_chemin = False

        #pareil que la collision joueur/qix mais en créant des coordonnées correspondant au chemin
        if len(tracage) > 1 :
            for i in range(len(tracage)):
                x1, y1 = tracage[i][0], tracage[i][1]
                if (x1 <= qx + 35 and x1 >= qx - 35 and y1 <= qy + 35 and y1 >= qy - 35):
                    qix_chemin = True
                
        if jeu == 2:
            qix2_chemin = False

            #pareil que la collision joueur/qix mais en créant des coordonnées correspondant au chemin
            if len(tracage2) > 1 :
                for i in range(len(tracage)):
                    x1, y1 = tracage[i][0], tracage[i][1]
                    if (x1 <= qx2 + 35 and x1 >= qx2 - 35 and y1 <= qy2 + 35 and y1 >= qy2 - 35):
                        qix2_chemin = True


        #--------------------------------------------------------------------------------
        #                               Collision joueur/sparx
        #--------------------------------------------------------------------------------

        #pareil que les précédentes collisions, en créant deux coordonnées pour les deux sparx puis en calculant la distance avec le joueur
        sparx_joueur = False
        rayon_sparx = 10  
            
        distance1 = ((p1 - x_sparx1) ** 2 + (q2 - y_sparx1) ** 2) ** 0.5
        distance2 = ((p1 - x_sparx2) ** 2 + (q2 - y_sparx2) ** 2) ** 0.5

        if distance1 < rayon_sparx + 10 or distance2 < rayon_sparx + 10 :
            sparx_joueur = True
        

       
    #--------------------------------------------------------------------------------
    #                              Collision joueur/pomme
    #--------------------------------------------------------------------------------

    #fais en sorte que si les coordonnées de la pomme et du joueur sont les mêmes, la pomme disparaît et rend le joueur invincible
    #en fait, on désactive les collisions 
        for p in pommes :
            if manger_pomme([p1, q2], p) :
                supprimer_pomme(p['tag'])
                mangee = time()

            if time() - mangee < 3 :
                if qix_joueur == True :
                    qix_joueur = False
                if sparx_joueur == True :
                    sparx_joueur = False
                if qix_chemin == True :
                    qix_chemin = False
                continue
                

        #--------------------------------------------------------------------------------
        #                                      Vies
        #--------------------------------------------------------------------------------

        #si le joueur entre en collision avec un ennemi, il perd une vie 
        if jeu == 1 :
            if qix_joueur or qix_chemin or sparx_joueur :
                vie -= 1
                if vie == 2 :
                    efface('Vie3')
                if vie == 1 :
                    efface('Vie2')
                if vie == 0 :
                    efface('Vie1')
                    perdu()
                    attend_ev()
                    ferme_fenetre()
                sleep(1)
                resX,resY = zone[1]
                p1,q1, p2,q2, p3,q3, p4,q4 = resX,resY+10,  resX+10,resY,  resX,resY-10,  resX-10,resY
                coordonnee2 = [p1, q2]
                coordonnee3 = [p1, q2]
                polycoordo = []
                tracage = []
                efface("trait")
        
        if jeu == 2 :
            if qix_joueur or qix_chemin or sparx_joueur or qix2_joueur or qix2_chemin :
                    vie -= 1
                    if vie == 2 :
                        efface('Vie3')
                    if vie == 1 :
                        efface('Vie2')
                    if vie == 0 :
                        efface('Vie1')
                        perdu()
                        attend_ev()
                        ferme_fenetre()
                    sleep(1)
                    resX,resY = zone[1]
                    p1,q1, p2,q2, p3,q3, p4,q4 = resX,resY+10,  resX+10,resY,  resX,resY-10,  resX-10,resY
                    coordonnee2 = [p1, q2]
                    coordonnee3 = [p1, q2]
                    polycoordo = []
                    tracage = []
                    efface("trait")
    
        if avant == "zone" :
            polycoordo = []

        if Zoneprise >= 75 :
            if jeu == 2:
                efface_tout()
                texte(400, 400, 'GG ! fin du jeu', 'White', ancrage='center', police='Times New Roman', tag='intro')
                mise_a_jour()
                sleep(2)
                break
            jeu = 2
            mise_a_jour()
            sleep(2)
            efface_tout()
            texte(400, 400, 'Niveau  2 !', 'White', ancrage='center', police='Times New Roman', tag='intro')
            sleep(2)
            rectangle (0,0,800,800, remplissage = "black" )
        
            rectangle(10,90 , 790,790, couleur = "Blue", epaisseur = 5)
            image(400 ,40 , 'GameTime.gif',largeur=300, hauteur=40, tag='GameTime')
            pomme()
            creer_obstacles()
            zone = [(10,90),(10,790),(790,790),(790,90)]
            zonejeu(zone)
            print(obstacles)

            p1,q1, p2,q2, p3,q3, p4,q4 = 400,800,  410,790,  400,780,  390,790  #point pour le losange (personnage)
            sommet = [p1,q1 , p2,q2, p3,q3 , p4,q4]
            tracage= []
            tracage2= []


            avant = "bord"
            avant2 = "bord"

            oldzone = 0
            newzone = 0
            Zoneprise = 0

            count=0         

            cotéprecedent = "Right"
            cotésuivent = "Right"

            cotéprecedent2 = "Up"
            cotésuivent2 = "Up"

            polycoordo = [(400,790)]

            polycoordo2 = [(790,350)]

            aire_totale = aire_poly(zone)

           
            texte(2, 0, f'Zone prise : {Zoneprise} %', 'White', police='Times New Roman', tag='zonepris')
            score = 0
            texte(2, 30, f'Score : {abs(score)} ', 'White', police='Times New Roman', tag='Score')
            personage (sommet)
            NB = 0
            qx, qy = 380, 405 #coordonnées du qix
            speed = 5  #vitesse du qix
            changement_direction = 10  #pour empêcher le tremblement

            countRepetition = 0



            vie = 3
            image(650 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie1')
            image(700 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie2')
            image(750 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie3')




        mise_a_jour() 

        countRepetition += 1

        countRepetition2 += 1
        coordoSurface = []

    ferme_fenetre()


if mode == 'joueur2' :
  #--------------------------------------------------------------------------------
    #                               Interface jeu
    #--------------------------------------------------------------------------------

    efface_tout()
    rectangle (0,0,800,800, remplissage = "black" )
 
    rectangle(10,90 , 790,790, couleur = "Blue", epaisseur = 5)

    Zoneprise = 0
    texte(160, 0, f'Zone prise  : {Zoneprise} %', 'White', police='Times New Roman', tag='zonepris1')
    score1= 0
    texte(160, 30, f'Score  : {abs(score)} ', 'White', police='Times New Roman', tag='Score2')

    Zoneprise2 = 0
    texte(400, 0, f'Zone prise 2 : {Zoneprise2} %', 'White', police='Times New Roman', tag='zonepris2')
    score2= 0
    texte(400, 30, f'Score 2 : {abs(score2)} ', 'White', police='Times New Roman', tag='Score2')
    pomme()


    p1,q1, p2,q2, p3,q3, p4,q4 = 10,350,  20,340,  10,330,  0,340
    sommet = [p1,q1, p2,q2, p3,q3, p4,q4]
    p12,q12, p22,q22, p32,q32, p42,q42 = 790,350,  800,340,  790,330,  780,340
    sommet2 = [p12,q12, p22,q22, p32,q32, p42,q42]
    personage (sommet)
    personage2 (sommet2)


    polycoordo = [(10,340)]


    cotéprecedent = "Up"
    cotésuivent = "Up"
    #--------------------------------------------------------------------------------
    #                               Images vies
    #--------------------------------------------------------------------------------

    #design des vies
    vie = 3
    image(650 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie12')
    image(700 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie22')
    image(750 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie32')

    vie2 = 3
    image(50 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie1')
    image(100 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie2')
    image(150 ,40 , 'Vie.gif',largeur=40, hauteur=40, tag='Vie3')


    zonejeu(zone)
    while True:


        if touche_pressee("Escape") :
            pause()
        efface ("pause")
        ev = donne_ev()
        tev = type_ev(ev)
        
        if type_ev(ev) == "Quitte":
            break
        cotéprecedent = cotésuivent
        traitdebut = (p1, q2) #debut du trait
        cordoPerso = (p1,q2)

        cotéprecedent2 = cotésuivent2
        traitdebut2 = (p12, q22) #debut du trait
        cordoPerso2 = (p12,q22)
        #print(polycoordo)
        f1,f2,f3,f4,k1,k2,k3,k4 = p1,p2,p3,p4,q1,q2,q3,q4
        if not ((touche_pressee("Left") or touche_pressee("Right")) and (touche_pressee("Up") or touche_pressee("Down"))) :
            if p1 > 10 and touche_pressee("Left"):
                p1,p2,p3,p4 = p1-dep, p2-dep, p3-dep, p4-dep
                cotésuivent = "Left"
            elif p1 < 790 and touche_pressee("Right"):
                p1,p2,p3,p4 = p1+dep,p2+dep,p3+dep,p4+dep
                cotésuivent = "Right" 
            elif q2 < 790 and touche_pressee("Down"):
                q1,q2,q3,q4 = q1+dep,q2+dep,q3+dep,q4+dep
                cotésuivent = "Down"
            elif q2 > 90 and touche_pressee("Up"):
                q1,q2,q3,q4 = q1-dep,q2-dep,q3-dep,q4-dep
                cotésuivent = "Up"
            if not qix_dans_polygone((p1,q2),zone) and not point_sur_bordure_polygone((p1,q2),zone):
                p1,p2,p3,p4,q1,q2,q3,q4=f1,f2,f3,f4,k1,k2,k3,k4
            
            traitfin = (p1, q2)

            if not point_sur_bordure_polygone((p1,q2),zone) :
                if touche_pressee("Return") and (touche_pressee("Left") or touche_pressee("Right") or touche_pressee("Up") or touche_pressee("Down")) :
                    trait(traitdebut,traitfin)   
                else:
                    
                    if p1 >= 10 and touche_pressee("Left"):
                        p1,p2,p3,p4 = p1+dep, p2+dep, p3+dep, p4+dep
                        cotésuivent = cotéprecedent
                        
                    elif p1 <= 790 and touche_pressee("Right"):
                        p1,p2,p3,p4 = p1-dep,p2-dep,p3-dep,p4-dep
                        cotésuivent = cotéprecedent
                    elif q2 <= 790 and touche_pressee("Down"):
                        q1,q2,q3,q4 = q1-dep,q2-dep,q3-dep,q4-dep
                        cotésuivent = cotéprecedent
                    
                    elif q2 >= 90 and touche_pressee("Up"):
                        q1,q2,q3,q4 = q1+dep,q2+dep,q3+dep,q4+dep
                        cotésuivent = cotéprecedent




        f12,f22,f32,f42,k12,k22,k32,k42 = p12,p22,p32,p42,q12,q22,q32,q42
        if not ((touche_pressee("q") or touche_pressee("d")) and (touche_pressee("z") or touche_pressee("s"))) :
            if p12 > 10 and touche_pressee("q"):
                p12,p22,p32,p42 = p12-dep, p22-dep, p32-dep, p42-dep
                cotésuivent2 = "Left"
            elif p12 < 790 and touche_pressee("d"):
                p12,p22,p32,p42 = p12+dep,p22+dep,p32+dep,p42+dep
                cotésuivent2 = "Right" 
            elif q22 < 790 and touche_pressee("s"):
                q12,q22,q32,q42 = q12+dep,q22+dep,q32+dep,q42+dep
                cotésuivent2 = "Down"
            elif q22 > 90 and touche_pressee("z"):
                q12,q22,q32,q42 = q12-dep,q22-dep,q32-dep,q42-dep
                cotésuivent2 = "Up"
            if not qix_dans_polygone((p12,q22),zone) and not point_sur_bordure_polygone((p12,q22),zone):
                p12,p22,p32,p42,q12,q22,q32,q42=f12,f22,f32,f42,k12,k22,k32,k42
            
            traitfin2 = (p12, q22)

            if not point_sur_bordure_polygone((p12,q22),zone) :
                if touche_pressee("space") and (touche_pressee("q") or touche_pressee("d") or touche_pressee("z") or touche_pressee("s")) :
                    trait2(traitdebut2,traitfin2)   
                else:
                    
                    if p12 >= 10 and touche_pressee("q"):
                        p12,p22,p32,p42 = p12+dep, p22+dep, p32+dep, p42+dep
                        cotésuivent2 = cotéprecedent2
                        
                    elif p12 <= 790 and touche_pressee("d"):
                        p12,p22,p32,p42 = p12-dep,p22-dep,p32-dep,p42-dep
                        cotésuivent2 = cotéprecedent2
                    elif q22 <= 790 and touche_pressee("s"):
                        q12,q22,q32,q42 = q12-dep,q22-dep,q32-dep,q42-dep
                        cotésuivent2 = cotéprecedent2
                    
                    elif q22 >= 90 and touche_pressee("z"):
                        q12,q22,q32,q42 = q12+dep,q22+dep,q32+dep,q42+dep
                        cotésuivent2 = cotéprecedent2


        coordoQix = (qx,qy)

        if type_ev(ev) == "Touche":
            coin_poly (cotéprecedent,cotésuivent)
            print (polycoordo)
        
        
            if not point_sur_bordure_polygone((p1,q2),zone):
                avant = "bord"
                tracage.append (traitfin)
            if point_sur_bordure_polygone((p1,q2),zone):
                polycoordo.append(traitfin)
                avant = "zone"

            if point_sur_bordure_polygone((p1,q2),zone) and len(polycoordo) >= 2 and polycoordo[0] != polycoordo[1] and avant == "zone":
                print("zone", zone)
                print("polycoordo",polycoordo) 
                coinAvantD = detect_sideDeb(polycoordo,zone)
                print(coinAvantD)
                coinAvantF = detect_sideFin(polycoordo,zone)
                print(coinAvantF)
                zone1 = poly1(coinAvantD,coinAvantF, polycoordo)
                zone2 = poly2(coinAvantD,coinAvantF, polycoordo)
                efface("zone")
                if qix_dans_polygone(coordoQix, zone1):
                    pris (zone2)
                    zonejeu(zone1)
                    zone = zone1
                else:
                    pris (zone1)
                    zonejeu(zone2)
                    zone = zone2
                efface("trait")
                print("zone restante", zone1)
                
                
                print("coin avant deb et fin",coinAvantD, "chef" , coinAvantF)
                print("polycoordo",zone2)
                 
                
                zoneair = aire_poly(zone)
                pourcentageZone = round((zoneair / aire_totale) * 100)
                Zoneprise = 100 - pourcentageZone
                NB +=1
                efface ('zonepris1')
                texte(160, 0, f'Zone prise : {Zoneprise} %', 'White', police='Times New Roman', tag='zonepris1')
                oldzone = Zoneprise
                score += (30-NB) * (newzone - oldzone) * 18 



                efface ('Score1')
                texte(160, 30, f'Score 1 : {abs(score)} ', 'White', police='Times New Roman', tag='Score1')



            if point_sur_bordure_polygone((p1,q2),zone):

                polycoordo = []
                tracage=[]


            coin_poly2 (cotéprecedent2,cotésuivent2)
            print (polycoordo2)
        
        
            if not point_sur_bordure_polygone((p12,q22),zone):
                avant2 = "bord"
                tracage2.append (traitfin2)
            if point_sur_bordure_polygone((p12,q22),zone):
                polycoordo2.append(traitfin2)
                avant2 = "zone"

            if point_sur_bordure_polygone((p12,q22),zone) and len(polycoordo2) >= 2 and polycoordo2[0] != polycoordo2[1] and avant2 == "zone":
                print("zone", zone)
                print("polycoordo2",polycoordo2) 
                coinAvantD2 = detect_sideDeb(polycoordo2,zone)
                print(coinAvantD2)
                coinAvantF2 = detect_sideFin(polycoordo2,zone)
                print(coinAvantF2)
                zone12 = poly1(coinAvantD2,coinAvantF2, polycoordo2)
                zone22 = poly2(coinAvantD2,coinAvantF2, polycoordo2)
                efface("zone")
                if qix_dans_polygone(coordoQix, zone12):
                    pris2 (zone22)
                    zonejeu(zone12)
                    zone = zone12
                else:
                    pris (zone12)
                    zonejeu(zone22)
                    zone = zone22
                efface("trait2")
                print("zone restante", zone12)
                
                
                print("coin avant deb et fin",coinAvantD2, "chef" , coinAvantF2)
                print("polycoordo",zone22)
                
                   
                zoneair2 = aire_poly(zone)
                pourcentageZone = round((zoneair2 / aire_totale) * 100)
                Zoneprise2 = 100 - pourcentageZone
                NB2 +=1
                efface ('zonepris2')
                texte(400, 0, f'Zone prise 2: {Zoneprise2} %', 'White', police='Times New Roman', tag='zonepris2')
                oldzone = Zoneprise
                score2 += (30-NB2) * (newzone - oldzone) * 18 



                efface ('Score2')
                texte(400, 30, f'Score 2 : {abs(score2)} ', 'White', police='Times New Roman', tag='Score2')

                oldzone = newzone


            if point_sur_bordure_polygone((p12,q22),zone):

                polycoordo2 = []
                tracage2=[]

        #--------------------------------------------------------------------------------
        #                               Mouvement sparx1
        #--------------------------------------------------------------------------------

        for sparx1 in lst_sparx1 :
            #attributs sparx1
            x_sparx1 = sparx1["x"]
            y_sparx1 = sparx1["y"]
            speed_sparx = sparx1["speed"]
            direction1 = sparx1["direction1"]

        #indications permettant au sparx de se déplacer en fonction de sa direction
        if direction1 == "up" :
            y_sparx1 -= speed_sparx
            if y_sparx1 < 90 :  
                direction1 = "right"
        elif direction1 == "down" :
            y_sparx1 += speed_sparx
            if y_sparx1 > 790:  
                direction1 = "left"
        elif direction1 == "left" :
            x_sparx1 -= speed_sparx
            if x_sparx1 < 10 :  
                direction1 = "up"
        else :  
            x_sparx1 += speed_sparx
            if x_sparx1 > 790 :  
                direction1 = "down"

        #limites sparx1 qui servent pour les directions
        x_sparx1 = min(max(x_sparx1, 10), 790)
        y_sparx1 = min(max(y_sparx1, 90), 790)

        #nouveaux attributs
        sparx1["x"] = x_sparx1
        sparx1["y"] = y_sparx1
        sparx1["direction1"] = direction1

        #dessine, supprime le sparx et le fait réapparaître pour le faire "se déplacer"
        efface("sparx1")
        cercle(x_sparx1, y_sparx1, 10, couleur="red", remplissage="red", tag="sparx1")



        #--------------------------------------------------------------------------------
        #                               Mouvement sparx2
        #--------------------------------------------------------------------------------

        for sparx2 in lst_sparx2 :
            #attributs sparx2
            x_sparx2 = sparx2["x"]
            y_sparx2 = sparx2["y"]
            speed_sparx = sparx2["speed"]
            direction2 = sparx2["direction2"]

        #indications permettant au sparx de se déplacer en fonction de sa direction
        if direction2 == "up" :
            y_sparx2 -= speed_sparx
            if y_sparx2 < 90 :  
                direction2 = "left"
        elif direction2 == "down" :
            y_sparx2 += speed_sparx
            if y_sparx2 > 790 : 
                direction2 = "right"
        elif direction2 == "right" :
            x_sparx2 += speed_sparx
            if x_sparx2 > 790 :  
                direction2 = "up"
        else :  
            x_sparx2 -= speed_sparx
            if x_sparx2 < 10 :  
                direction2 = "down"

        
        #limites sparx2 qui servent pour les directions
        x_sparx2 = min(max(x_sparx2, 10), 790)
        y_sparx2 = min(max(y_sparx2, 90), 790)

        #nouveaux attributs
        sparx2["x"] = x_sparx2
        sparx2["y"] = y_sparx2
        sparx2["direction2"] = direction2

        #dessine, supprime le sparx et le fait réapparaître pour le faire "se déplacer"
        efface("sparx2")
        cercle(x_sparx2, y_sparx2, 10, couleur="red", remplissage="red", tag="sparx2")



        #--------------------------------------------------------------------------------
        #                               Mouvement QIX
        #--------------------------------------------------------------------------------

        #if qui compte chaque tour de la boucle et uniformise la vitesse et les coordonnées pour que le qix ne tremble pas et soit fluide
        if countRepetition % changement_direction == 0 :
            fx, fy = random.uniform(-speed, speed), random.uniform(-speed, speed)    
        tx,ty = qx,qy
        qx, qy = qx + fx, qy + fy

        #dessine, supprime le qix et le fait réapparaître le faire "se déplacer"
        efface("Mechant")
        image(qx ,qy , 'Mechant.gif',largeur=70, hauteur=70, tag="Mechant")


        #--------------------------------------------------------------------------------
        #                               Limites pour le qix
        #--------------------------------------------------------------------------------

        #si les coordonnées du qix dépassent ces coordonnées, le qix revient là ce qui le bloque dans le cadre
        if not qix_dans_polygone((qx+35,qy),zone)\
        or not qix_dans_polygone((qx-35,qy),zone)\
        or not qix_dans_polygone((qx,qy+35),zone)\
        or not qix_dans_polygone((qx,qy-35),zone):
            qx,qy = tx,ty
            


        
        #--------------------------------------------------------------------------------
        #           Trait si pas déjà passer par la  / déplacement du perso
        #--------------------------------------------------------------------------------

        sommet = [p1,q1 , p2,q2, p3,q3 , p4,q4] 
        efface('perso')
        personage(sommet)

        sommet2 = [p12,q12 , p22,q22, p32,q32 , p42,q42] 
        efface('perso2')
        personage2(sommet2)


        #--------------------------------------------------------------------------------
        #                               Joueur dans zone 
        #--------------------------------------------------------------------------------
        
        joueur_zone = False

        if not qix_dans_polygone((p1,q2),zone) and not point_sur_bordure_polygone((p1,q2),zone):
            joueur_zone = True
        

         
        joueur_zone2 = False

        if not qix_dans_polygone((p12,q22),zone) and not point_sur_bordure_polygone((p12,q22),zone):
            joueur_zone2 = True
     

        #--------------------------------------------------------------------------------
        #                               Collision joueur/qix
        #--------------------------------------------------------------------------------

        qix_joueur = False

        #si les coordonnées du qix et du joueur sont les mêmes alors il y a collision
        if (p1 <= qx + 35 and p2 >= qx - 35 and q1 <= qy + 35 and q3 >= qy - 35) :
            qix_joueur = True



        qix_joueur2 = False

        #si les coordonnées du qix et du joueur sont les mêmes alors il y a collision
        if (p12 <= qx + 35 and p22 >= qx - 35 and q12 <= qy + 35 and q32 >= qy - 35) :
            qix_joueur2 = True



        #--------------------------------------------------------------------------------
        #                               Collision qix/chemin
        #--------------------------------------------------------------------------------

        qix_chemin = False

        #pareil que la collision joueur/qix mais en créant des coordonnées correspondant au chemin
        if len(tracage) > 1 :
            for i in range(len(tracage)):
                x1, y1 = tracage[i][0], tracage[i][1]
                if (x1 <= qx + 35 and x1 >= qx - 35 and y1 <= qy + 35 and y1 >= qy - 35):
                    qix_chemin = True
                


        qix_chemin2 = False

        #pareil que la collision joueur/qix mais en créant des coordonnées correspondant au chemin
        if len(tracage2) > 1 :
            for i in range(len(tracage2)):
                x12, y12 = tracage2[i][0], tracage2[i][1]
                if (x12 <= qx + 35 and x12 >= qx - 35 and y12 <= qy + 35 and y12 >= qy - 35):
                    qix_chemin2 = True


        #--------------------------------------------------------------------------------
        #                               Collision joueur/sparx
        #--------------------------------------------------------------------------------

        #pareil que les précédentes collisions, en créant deux coordonnées pour les deux sparx puis en calculant la distance avec le joueur
        sparx_joueur = False
        rayon_sparx = 10  
            
        distance1 = ((p1 - x_sparx1) ** 2 + (q2 - y_sparx1) ** 2) ** 0.5
        distance2 = ((p1 - x_sparx2) ** 2 + (q2 - y_sparx2) ** 2) ** 0.5

        if distance1 < rayon_sparx + 10 or distance2 < rayon_sparx + 10 :
            sparx_joueur = True
        


        sparx_joueur2 = False
            
        distance12 = ((p12 - x_sparx1) ** 2 + (q22 - y_sparx1) ** 2) ** 0.5
        distance22 = ((p12 - x_sparx2) ** 2 + (q22 - y_sparx2) ** 2) ** 0.5

        if distance12 < rayon_sparx + 10 or distance22 < rayon_sparx + 10 :
            sparx_joueur2 = True

        #--------------------------------------------------------------------------------
        #                              Collision joueur/pomme
        #--------------------------------------------------------------------------------

        for p in pommes:
            if manger_pomme([p1, q2], p):
                supprimer_pomme(p['tag'])
                mangee = time()

            if time() - mangee < 3 :
                qix_joueur == False and qix_chemin == False and sparx_joueur == False
                continue




        for p in pommes:
            if manger_pomme([p12, q22], p):
                supprimer_pomme(p['tag'])
                mangee = time()

            if time() - mangee < 3 :
                qix_joueur2 == False and qix_chemin2 == False and sparx_joueur2 == False
                continue
                
     
        #--------------------------------------------------------------------------------
        #                               Collision joueur/chemin
        #--------------------------------------------------------------------------------

        
        qix_chemin2 = False

        #pareil que la collision joueur/qix mais en créant des coordonnées correspondant au chemin
        if len(tracage2) > 1 :
            for i in range(len(tracage2)):
                x12, y12 = tracage2[i][0], tracage2[i][1]
                if (x12 <= qx + 35 and x12 >= qx - 35 and y12 <= qy + 35 and y12 >= qy - 35):
                    qix_chemin2 = True
                    

        #--------------------------------------------------------------------------------
        #                                      Vies
        #--------------------------------------------------------------------------------

        #si le joueur entre en collision avec un ennemi, il perd une vie 
        if qix_joueur or qix_chemin or sparx_joueur or joueur_zone:
            vie -= 1
            if vie == 2 :
                efface('Vie3')
            if vie == 1 :
                efface('Vie2')
            if vie == 0 :
                efface('Vie1')
                print('Vous avez perdu')
                sleep(1)
                texte(400, 400, 'Joueur 2 a gagne!', 'White', ancrage='center', police='Times New Roman', tag='intro')
                mise_a_jour()
                sleep(2)
                break
            sleep(1)
            resX,resY = zone[1]
            p1,q1, p2,q2, p3,q3, p4,q4 = resX,resY+10,  resX+10,resY,  resX,resY-10,  resX-10,resY
            cotéprecedent = ""
            cotésuivent = ""
            polycoordo = []
            tracage = []
            efface("trait")

            
    
        if avant == "zone" :
            polycoordo = []


        #même chose qu'avant pour le deuxième joueur
        if qix_joueur2 or qix_chemin2 or sparx_joueur2 or joueur_zone2:
            vie -= 1
            if vie == 2 :
                efface('Vie32')
            if vie == 1 :
                efface('Vie22')
            if vie == 0 :
                efface('Vie12')
                sleep(1)
                texte(400, 400, 'Joueur 1 a gagne!', 'White', ancrage='center', police='Times New Roman', tag='intro')
                mise_a_jour()
                sleep(2)
                break
            sleep(1)
            resX2,resY2 = zone[len(zone)-2]
            p12,q12, p22,q22, p32,q32, p42,q42 = resX2,resY2+10,  resX2+10,resY2,  resX2,resY2-10,  resX2-10,resY2
            cotéprecedent = ""
            cotésuivent = ""
            polycoordo2 = []
            tracage2 = []
            efface("trait2")
    
        if avant2 == "zone" :
            polycoordo2 = []
        
        totalpris = Zoneprise + Zoneprise2
        if totalpris >= 75 :
            if Zoneprise > Zoneprise2 :
                sleep(1)
                texte(400, 400, 'Joueur 1 a gagné !', 'White', ancrage='center', police='Times New Roman', tag='intro')
                mise_a_jour()

                sleep(2)
                break
            else :
                sleep(1)
                texte(400, 400, 'Joueur 2 a gagné !', 'White', ancrage='center', police='Times New Roman', tag='intro')
                mise_a_jour()
                sleep(2)
                break


        mise_a_jour() 

        countRepetition += 1
        coordoSurface = []

    ferme_fenetre()
