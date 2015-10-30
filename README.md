Для сборки, тестирования проекта использовались:
java 1.8.0_66 64-bit
maven 3.3.3
tomcat 8.0.28

Скрипты деплоя (проверял только в линуксе) - deploy2tomcat.sh(bat).
В скриптах задется урл к админке томката, пользователь и пароль.

В tomcat-users.xml необходимо задать роли для разрешения деплоя:
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<user username="tomcat" password="tomcat" roles="manager-script,manager-gui"/>

Проект деплоится по пути /CurrencyRateAPI
