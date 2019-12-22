#!/bin/bash
mkdir ~/.m2/
settings=$(cat << EOF
    <settings>
      <proxies>
       <proxy>
          <id>example-proxy</id>
          <active>true</active>
          <protocol>http</protocol>
          <host>172.30.1.1</host>
          <port>3128</port>
        </proxy>
      </proxies>
    </settings>
EOF
)
echo $settings > ~/.m2/settings.xml
cd ~/git
wget http://ftp.fau.de/eclipse/technology/epp/downloads/release/2018-12/R/eclipse-java-2018-12-R-linux-gtk-x86_64.tar.gz
tar -xvf eclipse*
rm *tar.gz
echo "export PATH=$PATH:/users/dut/info/$USER/git/projet_s2/iutScripts" >> ~/.bashrc
source ~/.bashrc
