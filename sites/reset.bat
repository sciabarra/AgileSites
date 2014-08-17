wget -O- http://localhost:8182
rd /s /q home
git checkout home
rd /s /q webapps
git checkout webapps
rd /s /q shared
git checkout shared
rd /s /q logs
git checkout logs
rd /s /q temp
git checkout temp


