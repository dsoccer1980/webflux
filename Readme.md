<H3>NoSQL, домашнее задание</H3>
В получившемся Reactive приложении, заменить хранилище паролей с реляционной БД на NoSQL.<BR>
Реализовать возможность получения Auth-event'ов от приложения через топик auth-events в Kafka.<BR>
В топик отправлять аудит-события вида <BR>
"user x123 successful log in", "Unknow user try to log in x123 user account",
"user x123 change password"

По полученному топику складывать метрики и отображать в Grafana в виде "Un/Successful log in"<BR>

![img.png](img.png)


`docker run -d -p 9090:9090 --name prometheus -v <path>prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus` <BR>
`docker run -d -p 3000:3000 grafana/grafana`  <BR>
`config/prometheus.yml`  <BR>
дашборд для графана: 
`config/dashboard/login.json`  <BR>

![img_1.png](img_1.png)