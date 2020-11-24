NAVIGOINNIN TEKO ALAS: https://www.youtube.com/watch?v=fODp1hZxfng


1) gradleen module: dependencies osioon isätään material 
implementation "com.google.android.material:material:1.2.1"
sitten paina sync now

2) mene res-folderiin, creare new adroid resourse directory
resource type: menu, OK
Menu hakemiston sisällä create new menu resource file
file name esim "nav_menu"
--> tuli nav_menu.xml
3) mene drawable kansioon, tee ikonit, jokaiselle navigoinni kohteelle omansa, max 5 kpl

4) lisää nav_menu.xml:ään vastaavasti itemeitä
jokaiselle itemille id, icon, title

5) Mene mainactivity.xml:ään
Relative layoutin sisään Framelayout
FrameLayoutin jälkeen bottomNavigationView
Framelaytoutiin layout_above-määritys

6) tee  hakemisto (package) MainActivityn rinnalle (onkohan välttämätön vaihe) Fragments

7) tee uusi blank Fragment, ota ruksi pois kohdasta Include fragment factory methods..
Tee loput fragmentit samoin

8) Mainactivityyn onCreate:een 
alusta fragmenteille muuttujat
tee funktio makeCurrentFragment, jossa replace:lla korvaat kulloisenkin fragmentin
onCreateen botton_navigation:lle onClick Listener













Uuden fragmentin lisääminen:
Mene resource-layout-kansioon

