#User documentation for the DataGalaxyServlet



# DataGalaxyServlet #

**Version beta1.0** Released: 2013-01-07
## Setting up DataGalaxyServlet ##
  1. Install a Servlet Controller (recommended to be Apache Tomcat 7.0.32)
  1. Checkout the DataGalaxyServlet project from GoogleCode:
```
    svn checkout http://ap27-cancer-genomics-linkage-app.googlecode.com/svn/projects/ DataGalaxyServlet/[release]
```
  1. Create the WAR file for DataGalaxyServlet
  1. Deploy the WAR file to the Servlet Controller <br /><p>If you have not set up the MySQL database required for the CollectionManager: </p>
  1. Create a MySQL database using the schema provided:
> > [CollectionManager\_Database\_Schema\_beta1.0.sql](http://code.google.com/p/ap27-cancer-genomics-linkage-app/downloads/detail?name=CollectionManager_Database_Schema_beta1.0.sql&can=2&q=)
## Configuring DataGalaxyServlet ##
  1. Go to: <tomcat dir>/webapps/DataGalaxyServlet/WEB-INF/classes/ and edit the 2 files:
    * hibernate.cfg.xml
    * mail.properties
  1. Edit file: hibernate.cfg.xml
    * Edit the URL to your MySQL database
    * Edit the MySQL-User
    * Edit the MySQL-Password <p>As an example:<br>
<pre><code>&lt;?xml version='1.0' encoding='utf-8'?&gt;<br>
&lt;!DOCTYPE hibernate-configuration PUBLIC<br>
"-//Hibernate/Hibernate Configuration DTD//EN"<br>
"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd"&gt;<br>
&lt;hibernate-configuration&gt;<br>
&lt;session-factory&gt;<br>
  &lt;property name="hibernate.connection.driver_class"&gt;com.mysql.jdbc.Driver&lt;/property&gt;<br>
  &lt;property name="hibernate.connection.url"&gt;jdbc:mysql://yourPathToMySQLDatabase&lt;/property&gt;<br>
  &lt;property name="hibernate.connection.username"&gt;yourMySQLUser&lt;/property&gt;<br>
  &lt;property name="hibernate.connection.password"&gt;yourMySQLPassword&lt;/property&gt;<br>
  &lt;property name="hibernate.connection.pool_size"&gt;10&lt;/property&gt;<br>
  &lt;property name="hibernate.current_session_context_class"&gt;thread&lt;/property&gt;<br>
  &lt;property name="show_sql"&gt;false&lt;/property&gt;<br>
  &lt;property name="dialect"&gt;org.hibernate.dialect.MySQLDialect&lt;/property&gt;<br>
  &lt;mapping class="org.qfab.domain.DataCollection"/&gt;<br>
  &lt;mapping class="org.qfab.domain.DataLocation"/&gt;<br>
  &lt;mapping class="org.qfab.domain.DataAdmin"/&gt;<br>
&lt;/session-factory&gt;<br>
&lt;/hibernate-configuration&gt;<br>
</code></pre>
</li></ul><blockquote></p>
</blockquote><ol><li>Edit file: mail.properties
    * mail.store.protocol=
    * mail.transport.protocol=smtp
    * mail.smtp.host=smtp.example.org
    * mail.smtp.user=
    * mail.debug=
    * mail.from=example@example.org
    * mail.host=