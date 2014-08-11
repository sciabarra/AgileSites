@ECHO OFF
if exist wcs\ofm_sites_generic_11.1.1.8.0_disk1_1of1.zip goto wcs118
if exist wcs\ofm_sites_generic_11.1.1.6_bp1_disk1_1of1.zip goto wcs116
cls
echo Please download WebCenter Sites 11.1.1.6.1 or 11.1.1.8.0 and place in the wcs folder.
start http://www.oracle.com/technetwork/middleware/webcenter/sites/downloads/index.html
goto pause
:wcs118
cd wcs
jar xvf ofm_sites_generic_11.1.1.8.0_disk1_1of1.zip WebCenterSites_11.1.1.8.0/WCS_Sites/WCS_Sites.zip
jar xvf WebCenterSites_11.1.1.8.0\WCS_Sites\WCS_Sites.zip
rd /s /q WebCenterSites_11.1.1.8.0
goto setup
:wcs116
cd wcs
jar xvf ofm_sites_generic_11.1.1.6_bp1_disk1_1of1.zip WebCenterSites_11.1.1.6_bp1/WCS_Sites_11.1.1.6_bp1/WCS_Sites_11.1.1.6_bp1.zip
jar xvf WebCenterSites_11.1.1.6_bp1\WCS_Sites_11.1.1.6_bp1\WCS_Sites_11.1.1.6_bp1.zip
rd /s /q WebCenterSites_11.1.1.6_bp1
goto setup
:setup
call setup
goto pause
:pause
pause
cls
