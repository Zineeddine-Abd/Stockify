first add those jar files:
"fontawesomefx-commons-9.1.2.jar" and "fontawesomefx-fontawesome-4.7.0-9.1.2.jar"
to classpath
Right click on project ->
Build path -> 
Configure build path... ->
Libraries (Tab) ->
select classpath ->
Add External JARs ->
then go to "SUPER IMPORTANT JAR FILES FOR ICONS ADD TO CLASSPATH" folder ->
Apply and Close

_________________________________________________________
second if you face an issue: cannot open "loginScene.fxml" using Scene builder
you can open another fxml file like "adminScene.fxml"
then in LEFT SIDE you will see "Library", "Document" sections
go to "Library" and click on the small setting icon ->
JAR/FXML Manager -> 
Search repositories ->
type "fontawesomefx" in search box ->
click on "Search" button ->
choose "de.jensd:fontawesomefx-fontawesome" -> click Add JAR
repeat the same process to add "de.jensd:fontawesomefx-commons"