29.11.2020 Tianne:
**KATEGORIA
 TODO:Uuden kategorian lisääminen, entisen poistaminen, näytön refressaus. Kuvan lisäys/muuttaminen (KTS LINKKEJÄ TÄMÄN DOKUMENTIN LOPUSTA)
**TUOTE
  TODO: refreshView ei toimi aina....
**OSTOSLISTA
   TODO: tekstin tasaus vasemmalle
   TODO: Kaikkien checkboxien tyhjäys/täyttö kerralla
 **YLEISESTI
    Sivun nimen näyttäminen yläpalkissa "Ostoslista"-sijaan  (Kategoriat, Kategorian tuotteet, Tuotteet, Ostoslista)
------------------------------------
Vinkkejä refressukseen recyclerView:sta:
https://www.tutorialkart.com/kotlin-android/android-refresh-listview-example/
https://www.youtube.com/watch?v=d00e-kvuF9k
https://www.youtube.com/watch?v=dAcuZRmA5q4
-------------
3.12.2020: Muutettu näkymään otsikon "recyclerview" sijaan "Ostoslista". Jos halutaan muuttaa sovelluksesta käsin, kts: https://developer.android.com/training/appbar/setting-up
--------------
recyclerin napin klikkaus kiinni productFragmentissa
    https://developer.android.com/codelabs/kotlin-android-training-interacting-with-items#3

Miten välittää dataa fragmentilta mainactivitylle:
https://stackoverflow.com/questions/47850746/how-to-pass-data-from-fragment-to-mainactivity

Miten siirtyä fragmentilta toiselle
https://stackoverflow.com/questions/23212162/how-to-move-from-one-fragment-to-another-fragment-on-click-of-an-imageview-in-an

How do I open a new fragment from another fragment?
https://stackoverflow.com/questions/21028786/how-do-i-open-a-new-fragment-from-another-fragment

Miten siirtää dataa fragmentilta toiselle
https://developer.android.com/guide/fragments/communicate

Communicate with other fragments | Android Developersdeveloper.android.com › fragments
27.4.2020 — The fragment uses the callback interface to deliver the event to the parent activity. Kotlin Java More. override fun onListItemClick ...

Dialogi:
https://developer.android.com/reference/android/app/DialogFragment
https://devofandroid.blogspot.com/2018/04/alertdialog-with-custom-layout-kotlin.html
Leijuva button:
https://devofandroid.blogspot.com/2018/03/adding-floating-action-button-kotlin.html

PUNAVIHREÄ-sokeus testi: https://asada.website/webCVS/
KUVAT: vector vai image:https://stackoverflow.com/questions/44357112/image-asset-versus-vector-asset
KÄYTTÄJÄ VALITSEE KUVAN: https://stackoverflow.com/questions/10165302/dialog-to-pick-image-from-gallery-or-from-camera
(JAVA) https://zaidisoft.com/android-imageview-display-image-from-gallery-or-camera/
https://developer.android.com/training/camera/photobasics
----------------------------------------
Tämä navigointi aika alkuvaiheessa sovelluksen tekoa!
NAVIGOINNIN TEKO ALAS: https://www.youtube.com/watch?v=fODp1hZxfng

1) gradleen module: dependencies osioon isätään material 
implementation "com.google.android.material:material:1.2.1"
sitten paina sync now
2) mene res-folderiin, create new adroid resourse directory
resource type: menu, OK
Menu hakemiston sisällä create new menu resource file
file name esim "nav_menu"
--> tuli nav_menu.xml
3) mene drawable kansioon, tee ikonit, jokaiselle navigoinnin kohteelle omansa, max 5 kpl
(new vector asset)
4) lisää nav_menu.xml:ään vastaavasti itemeitä
jokaiselle itemille id, icon, title
---> No niin, tässä kun en heti muistanut laittaa id:tä kohdilleen jokaiselle, niin onkin sitten vaikea muuttaa jälkikäteen!!!!!!!!!
5) Mene mainactivity.xml:ään
Relative layoutin sisään Framelayout
FrameLayoutin jälkeen bottomNavigationView
Framelaytoutiin layout_above-määritys
6) tee  hakemisto (package) MainActivityn rinnalle (onkohan välttämätön vaihe) Fragments
(---En tehnyt tätä, vaan laitoin kaikki tuonne samalle tasolle)
7) tee uusi blank Fragment, ota ruksi pois kohdasta Include fragment factory methods..
Tee loput fragmentit samoin
(---minulle ei tullut tuota ruksia mhdolliseksi valita, tuli Kategory.kt tiedosto ja fragment_katoegory.xml -layout-tiedosto
8) Mainactivityyn onCreate:een 
 8.1. alusta fragmenteille muuttujat
 8.2. tee funktio makeCurrentFragment, jossa replace:lla korvaat kulloisenkin fragmentin
 8.3 onCreateen botton_navigation:lle onClick Listener
------
