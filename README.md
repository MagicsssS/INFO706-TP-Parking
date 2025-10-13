# Un exemple minimaliste d'application Jakarta EE 10 (web profile) #


Pour faire simple l'application est un _Hello world_ très basique.

## Implantation ## 

L'application comporte uniquement
- une Servlet, [`HelloServlet`](src/main/java/fr/usmb/jee/HelloServlet.java) et 
- une page JSP, [`Hello.jsp`](src/main/webapp/Hello.jsp)

### La Servlet ###

La Servlet généralement est définit comme une sous classe de [`HttpServlet`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpservlet).  
L'annotation [`@WebServlet`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/annotation/webservlet) permet la déclarer au serveur JEE et de choisir la ou les URLs associées à la Servlet :

```java
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
    ...
}
```

En fonction des méthodes HTTP auxquelles on veut pouvoir répondre, on va surcharger les méthodes 
_doXxx_ correspondantes ([`doGet`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpservlet#doGet-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-), [`doPost`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpservlet#doPost-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-), [`doPut`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpservlet#doPut-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-), [`doDelete`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpservlet#doDelete-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-), etc.).
Chacune de ces méthodes prend deux paramètres de type [`HttpRequest`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpservletrequest)
et [`HttpResponse`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpservletresponse),
permettant de manipuler respectivement la _requête http_ et la _réponse http_ à renvoyer.  
Dans notre cas seules les méthodes [`doGet`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpservlet#doGet-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-)
et [`doPost`](https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpservlet#doPost-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-) ont été surchargées :

```java
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        PrintWriter res = response.getWriter();
        res.println("Hello "+ name + " !");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
```

### La page JSP ###

Les pages JSP sont des composants web (typiquement des pages HTML ou XML) dans lesquels on va pouvoir appeler du code Java 
ou insérer des parties générées à partir du contenu de la _requête http_ ou de la _session de l'utilisateur_
(dans notre exemple, [`Hello.jsp`](src/main/webapp/Hello.jsp), la valeur du paramètre `name` de la _requête http_ est inséré dans la réponse renvoyée).  
Par défaut, les `pages JSP` sont compilées par le serveur JEE, sous forme de Servlet, lors de le leur 1er appel.
 
## Packaging ##

Comme nous sommes dans le cas d'une application web, l'application peut être empaquetée dans une archive web (`hello-maven.war`).

Les ressources web (pages html, JSP, feuilles de styles CSS, etc.), contenues dans le dosser [`src/main/webapp`](src/main/webapp), sont ajoutées à la racine du fichier d'archive.

Deux dossiers dossiers spécifiques, `META-INF` et `WEB-INF` dans l'archive `war` permettent de configurer l'application web et de gérer les parties en java.

<pre>
hello-maven.war
  |-- <a href="src/main/webapp/Hello.jsp" >Hello.jsp</a> (Page JSP the type _Hello World_)
  |-- <a href="src/main/webapp/META-INF/MANIFEST.MF" >META-INF/MANIFEST.MF</a> (java manifeste)
  |-- WEB-INF/web.xml (descripteur standard de l'application Web -- optionnel dans les dernières versions de JEE)
  |-- WEB-INF/lib (librairies supplémentaires java utilisées par les classes java - permet d'embarquer des _fichiers jar_ dans l'application web)
  |-- WEB-INF/classes (classes java : servlets et autres classes java)
                |-- <a href="src/main/java/fr/usmb/jee/HelloServlet.java" >fr/usmb/jee/HelloServlet.class</a> (Servlet de démo)
</pre>

## Usage : ##

### Build ###

Pour voir les sources, il suffit de cloner le projet git et de l'importer (sous forme de projet [Maven](https://maven.apache.org/)) dans votre IDE favori. 
Cela devrait permettre la création d'un projet (ou module) web.

La compilation des classes et la création de l'archive _war_ peut se faire via [Maven](https://maven.apache.org/)
en appelant la tâche `package` (où la tâche `war:war`) sur le projet principal :

```bash
./mvnw package
```

La configuration Maven (cf. fichier [pom.xml](pom.xml)),
utilise 
- le [compiler plugin](https://maven.apache.org/plugins/maven-compiler-plugin/) pour la compilation des classes java et
- le [plugin war](https://maven.apache.org/plugins/maven-war-plugin/) pour l'assemblage de l'application Web et la génération de l'_archive war_.

Dans le fichier de _build Maven_, [pom.xml](pom.xml), une dépendance envers le [profil web Jakarta EE 10](https://jakarta.ee/specifications/webprofile/10/)
permet de disposer des APIs incluses dans ce profil lors de la compilation de la Servlet :

```xml
  <dependencies>
    <!-- dependance pour le profil web de Jakarta EE 10-->
      <dependency>
        <groupId>jakarta.platform</groupId>
        <artifactId>jakarta.jakartaee-web-api</artifactId>
        <version>10.0.0</version>
        <!-- for compilation only and provided by runtime-->
        <scope>provided</scope>
      </dependency>
  </dependencies>
```

### Utilisation avec un serveur Jakarta EE 10 local ###

Pour utiliser l'exemple il suffit de déployer le fichier _hello-maven.war_ sur un [serveur Jakarta EE 10](https://jakarta.ee/compatibility/certification/10/).

Le client Web est alors déployé avec le préfixe _/hello-maven/_.  
Il devrait donc être accessible à l'adresse http://localhost:8080/hello-maven/HelloServlet?name=Joe pour la servlet
et http://localhost:8080/hello-maven/Hello.jsp?name=Jeanne pour la page JSP.

## Quelques Tutoriels ##

- [Tutoriel en français sur les Servlet de J-M Doudoux (JavaEE 8)](https://www.jmdoudoux.fr/java/dej/chap-servlets.htm)
- [Tutoriel en français sur les pages JSP de J-M Doudoux (JavaEE 8)](https://www.jmdoudoux.fr/java/dej/chap-jsp.htm)
- [Tutoriel en français sur les Servlet de Julien Gilli (JavaEE 8)](https://java.developpez.com/tutoriels/servlets-java/?page=les-servlets-http)
- [Tutoriel Jakarta EE sur les Servlet - en anglais](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/web/servlets/servlets.html)

## Documentation : ##

Jakarta EE 10 (Fondation Eclipse)
- Doc : https://jakarta.ee/resources/#documentation
- API (javadoc) : https://jakarta.ee/specifications/platform/10/apidocs/
- Spécifications : https://jakarta.ee/specifications
- Serveurs compatibles : 
    - https://jakarta.ee/compatibility/certification/10/

Jakarta EE 9 (Fondation Eclipse)
- Doc : https://jakarta.ee/resources/#documentation
- Tutoriel : https://eclipse-ee4j.github.io/jakartaee-tutorial/
- API (javadoc) : https://jakarta.ee/specifications/platform/9/apidocs/
- Spécifications : https://jakarta.ee/specifications
- Serveurs compatibles : 
    - https://jakarta.ee/compatibility/certification/9/
    - https://jakarta.ee/compatibility/certification/9.1/

Jakarta EE 8 (Fondation Eclipse)
- Doc : https://javaee.github.io/glassfish/documentation
- Tutoriel : https://javaee.github.io/tutorial/
- API (javadoc) : https://jakarta.ee/specifications/platform/8/apidocs/
- Spécifications : https://jakarta.ee/specifications
- Serveurs compatibles : https://jakarta.ee/compatibility/certification/8/

Java EE 8 (Oracle)
- Doc : https://javaee.github.io/glassfish/documentation
- Tutoriel : https://javaee.github.io/tutorial/
- API (javadoc) : https://javaee.github.io/javaee-spec/javadocs/
- Spécifications : https://www.oracle.com/java/technologies/javaee/javaeetechnologies.html#javaee8
- Serveurs compatibles : https://www.oracle.com/java/technologies/compatibility-jsp.html

Java EE 7 (Oracle)
- Doc : http://docs.oracle.com/javaee/7
- Tutoriel : https://docs.oracle.com/javaee/7/tutorial
- API (javadoc) : http://docs.oracle.com/javaee/7/api
- Spécifications : https://www.oracle.com/java/technologies/javaee/javaeetechnologies.html#javaee7
