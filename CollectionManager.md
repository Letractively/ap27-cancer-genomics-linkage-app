#User documentation for the CollectionManager



# CollectionManager #

**Version beta1.0** Released: 2013-01-07
## Setting up CollectionManager ##
  1. Install a Servlet Controller (recommended to be Apache Tomcat 7.0.32)
  1. Add mysql-connector-java-5.1.15.jar to <tomcat dir>/lib/
  1. Go to <tomcat dir>/conf/ and edit file server.xml
    * Comment the line
```
<!-- <Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase"/> -->
```
    * Add instead
```
<Realm allRolesMode="authOnly" className="org.apache.catalina.realm.JDBCRealm"
                connectionName="yourMySQLUser" connectionPassword="yourMySQLPassword"
                connectionURL="jdbc:mysql://pathToYourMySQLDatabase/dbName"
                driverName="com.mysql.jdbc.Driver" userCredCol="password"
                digest="sha" userNameCol="email" userTable="person"/>
```
  1. Create a MySQL database using the schema provided:
> > [CollectionManager\_Database\_Schema\_beta1.0.sql](http://code.google.com/p/ap27-cancer-genomics-linkage-app/downloads/detail?name=CollectionManager_Database_Schema_beta1.0.sql&can=2&q=)
  1. Checkout the DataSourceManager project from GoogleCode:
```
    svn checkout http://ap27-cancer-genomics-linkage-app.googlecode.com/svn/projects/ DataSourceManager/[release]
```
  1. Add the Hibernate JAR files in the Build Path of DataSourceManager (The Jars are available in the Download section).
  1. Add external JAR files in the Build Path of DataSourceManager (jboss,javassist,dom4j,c3p0 and antlr) available in the Download section.
  1. Create the JAR file for DataSourceManager
  1. Checkout the CollectionInterface project from GoogleCode:
```
    svn checkout http://ap27-cancer-genomics-linkage-app.googlecode.com/svn/projects/ CollectionInterface/[release]
```
  1. Add DataSourceManager.jar as library to the CollectionInterface
  1. Create the WAR file for CollectionInterface
  1. Deploy the WAR file to the Servlet Controller


## Configuring CollectionManager ##
  1. Go to: <tomcat dir>/webapps/CollectionInterface/WEB-INF/classes/ and edit the 3 files:
    * hibernate.cfg.xml
    * config.properties
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
   &lt;property name="show_sql"&gt;false&lt;/property&gt;<br>
  &lt;property name="dialect"&gt;org.hibernate.dialect.MySQLDialect&lt;/property&gt;<br>
  &lt;!-- Enable Hibernate's current session context --&gt;<br>
  &lt;property name="current_session_context_class"&gt;org.hibernate.context.internal.ManagedSessionContext&lt;/property&gt;<br>
  &lt;property name="hibernate.cache.use_second_level_cache"&gt;false&lt;/property&gt;<br>
  &lt;property name="hibernate.cache.use_query_cache"&gt;false&lt;/property&gt;<br>
  &lt;!-- Disable the second-level cache  --&gt;<br>
  &lt;property name="cache.provider_class"&gt;org.hibernate.cache.NoCacheProvider&lt;/property&gt;<br>
  <br>
  &lt;property name="c3p0.acquire_increment"&gt;1&lt;/property&gt;<br>
  &lt;property name="c3p0.idle_test_period"&gt;100&lt;/property&gt;<br>
  &lt;!-- seconds --&gt;<br>
  &lt;property name="c3p0.max_size"&gt;100&lt;/property&gt;<br>
  &lt;property name="c3p0.max_statements"&gt;0&lt;/property&gt;<br>
  &lt;property name="c3p0.min_size"&gt;10&lt;/property&gt;<br>
  &lt;property name="c3p0.timeout"&gt;100&lt;/property&gt;<br>
  <br>
  &lt;mapping class="org.qfab.domains.DataSource"/&gt;<br>
  &lt;mapping class="org.qfab.domains.DataLocation"/&gt;<br>
  &lt;mapping class="org.qfab.domains.Person"/&gt;<br>
  &lt;mapping class="org.qfab.domains.Site"/&gt;<br>
  &lt;mapping class="org.qfab.domains.Institution"/&gt;<br>
  &lt;mapping class="org.qfab.domains.SubjectCode"/&gt;<br>
 &lt;/session-factory&gt;<br>
&lt;/hibernate-configuration&gt;<br>
</code></pre>
</li></ul><blockquote></p>
</blockquote><ol><li>Edit file: config.properties
    * to.email= yourAdminEmailAddress
    * rifcs.dir= pathToWhereRIFCSShouldBeWritten
    * server.production=
    * origination.source=
    * rifcs.dir.test= <p>As an example:<br>
<pre><code>#Email information<br>
from.email=<br>
to.email=[admin.email]<br>
subject.email=New DataSource Site<br>
body.url.email=<br>
<br>
#Directory to write the RIFCS to<br>
rifcs.dir=path/where/rifcs/will/be/generated<br>
server.production=http://server.name:8080/ProjectName<br>
originating.source=http://server.name:8080/oaiPMHservlet/admin/data-provider.do<br>
rifcs.dir.test=path/where/rifcs/will/be/generated/test<br>
</code></pre>
</li></ol><blockquote></p>
</blockquote><ol><li>Edit file mail.properties:
    * mail.host=DataSource Manager
    * mail.from=no-reply@datasource.org
    * mail.from.internet.name=Collection Manager
    * mail.store.protocol=
    * mail.transport.protocol=
    * mail.smtp.host=
    * mail.smtp.user=
    * mail.debug=
    * to.email=yourEmailAddress