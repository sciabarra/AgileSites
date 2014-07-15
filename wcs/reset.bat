wget -O- http://localhost:8182
rd /s /q home
git checkout home
rd /s /q webapps
git checkout webapps
rd /s /q shared
git checkout shared

