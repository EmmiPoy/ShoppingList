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
25.11.2020 17:30
muutettu: product_row.xml (lisätty kategoria nappi, säädetty kenttien paikkaa, ei vielä ok)
Klikkaus kiinni productFragmentissa.
    https://developer.android.com/codelabs/kotlin-android-training-interacting-with-items#3
