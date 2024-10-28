*please copy this "lib" folder to your "C" disk part
*and you need to add "JavaFX" user library (use the same library name) including java fx libraries
*to add a user library click "window" menu, then "preferences", then search for "user libraries" , and add new one then add external jars to it
*because we did not use a build automation tool (like maven) to work with on this project


_________________________________________________________
"Scene builder"


if you face this issue: cannot open "loginScene.fxml" using Scene builder
you can open another fxml file like "adminScene.fxml"
then in LEFT SIDE you will see "Library" & "Document" sections
go to "Library" and click on the small setting icon ->
"JAR/FXML Manager" -> 
"Search repositories" ->
type "fontawesomefx" in search box ->
click on "Search" button ->
choose "de.jensd:fontawesomefx-fontawesome" -> click Add JAR
repeat the same process to add "de.jensd:fontawesomefx-commons"