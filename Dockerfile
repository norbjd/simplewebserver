FROM    openjdk:8u171-jre-alpine3.8

COPY    target/simplewebserver-*-bin.tar.gz /tmp/simplewebserver.tar.gz

RUN     mkdir -p /opt/simplewebserver \
 &&     tar -xzf /tmp/simplewebserver.tar.gz -C /opt/simplewebserver

CMD     java \
            -cp $(find /opt/simplewebserver/ -name '*.jar' | \
                    tr '\n' ':' | \
                    sed 's/:$//g') \
            com.norbjd.simplewebserver.App
