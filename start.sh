#! /bin/sh

usage() {
	echo "用法：./start.sh [-options]"
	echo "其中选项包括："
	echo "    -f                                           执行dev的flyway操作"
	echo "    -g                                           执行codegen操作"
	echo "    -f -g                                        执行flyway以及codegen操作"
}
usageForRun() {
  echo "请按照以下命令使用'-r'参数：\n"
  echo "./start.sh -r xwlb-api        启动xwlb-api"
  echo "./start.sh -r xwlb-scheduler  启动xwlb-scheduler"
  echo "\n"
}

while getopts ":fguprdm" opt; do
	case $opt in
		f)
			echo "\n=========== start flyway  ==========\n"
            cd xwlb-core
            mvn -P dev config:injectProperties flyway:migrate
            cd ..
			echo "\n=========== end  flyway  ==========\n"
			;;
		g)
			echo "\n============ start codegen ==========\n"
			cd xwlb-core
			mvn -P dev config:injectProperties jooq-codegen:generate
			cd ..
			echo "\n============ end codegen ============\n"
			;;
	esac
done
