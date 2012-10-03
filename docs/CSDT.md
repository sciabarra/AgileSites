# CSDT

Content Server Development Tool is integrated in sbt.

Setup will configure csdt to use the folder export/envision inside the project.

The command to use csdt is ``wcs-csdt`` followed by one or more arguments.

- ``wcs-csdt`` returns  a list of assets in the selected site
- ``wcs-csdt export`` exports all the assets from the current site
- ``wcs-csdt import`` imports all the assets in the current site

# Other examples

The correct synopsis is ``wcs-csdt command selection``, where:

Command defaults to ``listcs`` but can be

- ``listcs``
- ``listds``
- ``export``
- ``import`` 

Selection defaults to ``@ALL_NONASSETS;@ALL_ASSETS`` but can be any syntax supported by CSDT.

See the WCS Developer Tools documentation for more details.



