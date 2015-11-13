# Cancer Genomics Linkage App #

This project is composed of a number of components:

  * **Data Synchronisation**
Reference datasets from public repositories can be mirrored locally using the download scheduler [BioMAJ](http://biomaj.genouest.org/). See [Data\_Synchronisation\_Setup.pdf](http://code.google.com/p/ap27-cancer-genomics-linkage-app/downloads/detail?name=Data_Synchronisation_Setup.pdf&can=2&q=) for more information about setting up BioMAJ. To link the data downloaded from BioMAJ with Galaxy see GalaxyDataLinkTool.

  * **DataLink Tool**
Linking downloaded reference data with Galaxy

  * **Collection Manager**
This is a web-interface that allows users to manage the data and workflow records that are published to the RDA website. See CollectionManager on how to host a local instance.

  * **Data Galaxy Servlet**
Linking datasets downloaded with [BioMAJ](http://biomaj.genouest.org/) with the Collection Manager. See DataGalaxyServlet for more information about how to set up the servlet.

  * **Galaxy Instance**
In order to publish workflow from Galaxy to RDA, a button has been added into Galaxy.

A new fork of the galaxy central repository has been implemented and users can download a customised version of the software with the extra feature.
This repository can be found under https://bitbucket.org/qfab/galaxy-dist