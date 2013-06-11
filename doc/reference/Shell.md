# Agile Shell



## setup

- wcs-setup-offline [satellite] [silent]

- wcs-setup-online [silent]


## compile

Main compilation method:

- wcs-package-jar

to be used in continuos mode (`~ wcs-package-jar`)

- wcs-deploy

-- wcs-package-jar
-- wcs-copy-static
-- wcs-update-model

## log

- wcs-log view

- wcs-log list

- wcs-log trace mysite

- wcs-log trace wcs 4445

## csdt

- wcs-dt 

default: listcs

- wcs-dt listcs [asset-selector ...]

- wcs-dt listds [asset-selector ...]

- wcs-dt export [asset-selector ...]

default: @SITE @ASSET_TYPE @ALL_ASSET @ALL_NONASSET

- wcs-dt import [asset-selector ...]

default: @SITE @ASSET_TYPE @ALL_ASSET @ALL_NONASSET

- wcs-dt import [asset-selector ...]
