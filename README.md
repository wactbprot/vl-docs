```
       _           _
__   _| |       __| | ___   ___ ___
\ \ / / |_____ / _` |/ _ \ / __/ __|
 \ V /| |_____| (_| | (_) | (__\__ \
  \_/ |_|      \__,_|\___/ \___|___/

```


## customer

### url scheme
`/customer/<customer-id>`

### example:
http://localhost:8012/customer/cus-ptb_ag_7.54


## generate uberjar (tools.build)

In order to generate an stand alone uberjar **vl-docs** uses [tools.build](https://clojure.org/guides/tools_build). Run:

```shell
clj -T:build clean
clj -T:build prep
clj -T:build uber
# or
clj -T:build all
```

upload:

```shell
scp -r target/vl-docs-x.y.z.jar bock04@a75438://var/www/html/vl-docs/
```

Start the server by invoking:

```shell
java -jar target/vl-docs-x.y.z.jar
```

## systemd

```shell
cd /path/to/vl-docs
sudo mkdir /usr/local/share/vl-docs
sudo cp vl-docs.jar /usr/local/share/vl-docs
sudo cp vl-docs.service  /etc/systemd/system/
sudo systemctl enable vl-docs.service
sudo systemctl start vl-docs.service
sudo systemctl status vl-docs.service
```
