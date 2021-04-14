#! /bin/sh

usage() {
  echo "用法：./start.sh [-options]"
  echo "其中选项包括："
  echo "    -f                                           执行dev的flyway操作"
  echo "    -b                                           执行flyway baseline操作"
  echo "    -g                                           执行codegen操作"
  echo "    -f/-b -g                                     执行flyway以及codegen操作"
  echo "    -r                                           运行程序"
}
usageForRun() {
  echo "请按照以下命令使用'-r'参数：\n"
  echo "./start.sh -r xwlb-api        启动xwlb-api"
  echo "./start.sh -r xwlb-admin        启动xwlb-admin"
  echo "./start.sh -r xwlb-scheduler  启动xwlb-scheduler"
  echo "\n"
}

while getopts ":fbgr" opt; do
  case $opt in
  f)
    echo "\n=========== start flyway  ==========\n"
    cd xwlb-core
    mvn -P dev flyway:migrate
    cd ..
    echo "\n=========== end  flyway  ==========\n"
    ;;
  b)
    echo "\n=========== start flyway baseline ==========\n"
    cd xwlb-core
    mvn -P dev flyway:baseline flyway:migrate
    cd ..
    echo "\n=========== end  flyway baseline ==========\n"
    ;;
  g)
    echo "\n============ start codegen ==========\n"
    cd xwlb-core
    mvn -P dev jooq-codegen:generate
    cd ..
    echo "\n============ end codegen ============\n"
    ;;
  r)
    echo "\n=========== start run ============\n"
    if [[ $2 ]]; then
      if [[ $3 ]]; then
        mvn clean -P dev package -pl $2 -am -Dmaven.test.skip=true -DapiMainClass=$3
      else
        mvn clean -P dev package -pl $2 -am -Dmaven.test.skip=true
      fi
      cd $2/target
      java -jar $2.jar --spring.profiles.active=dev
      cd ../..
      echo "\n=========== end run ============\n"
    else
      usage
    fi
    ;;
  esac
done
