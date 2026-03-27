#!/bin/bash
# ==============================================
#  Lilishop 多模块自动部署脚本
#  功能：自动识别最新 jar，自动杀进程，自动启动
#  环境：宝塔 JDK21（全局默认，无需指定路径）
# ==============================================

# ===================== 统一配置（只改这里） =====================
# 部署目录（与 deploy.yml 变量保持一致）
DEPLOY_DIR="/www/wwwroot/java"

# JVM 内存参数
JVM_OPTS="-Xmx1024M -Xms256M"

# Spring 环境：生产环境
SPRING_PROFILE="--spring.profiles.active=prod"

# 模块名与端口映射
declare -A PROJECT_MAP=(
  ["manager-api"]=8887
  ["buyer-api"]=8888
  ["seller-api"]=8889
  ["common-api"]=8890
  ["im-api"]=8885
  ["consumer"]="8891 8886"
)
# =================================================================

# 出错继续执行，不中断
set -e

# 进入 jar 所在目录
cd "${DEPLOY_DIR}"

# ===================== 函数：根据端口杀死进程 =====================
kill_by_port() {
  local port="$1"
  pid=$(lsof -t -i:"$port" 2>/dev/null | head -n 1)
  
  if [ -n "$pid" ]; then
    kill -9 "$pid" 2>/dev/null
    sleep 1
    echo "✅ 端口 $port 旧进程已清理"
  fi
}

# ===================== 函数：自动启动项目（自动找最新 jar） =====================
start_project() {
  local module="$1"
  local port="$2"

  # 自动匹配最新版本 jar
  local jar=$(ls | grep -E "^$module-[0-9].*\.jar$" | tail -n 1)

  if [ -z "$jar" ]; then
    echo "❌ 未找到 $module 的 jar，跳过"
    return
  fi

  echo "====================================="
  echo "🚀 启动：$module"
  echo "🔌 端口：$port"
  echo "📦 自动使用最新 jar：$jar"

  # ==================== 启动命令（纯净版，自动使用系统默认 JDK21） ====================
  nohup java -jar $JVM_OPTS "$jar" \
    --server.port="$port" \
    $SPRING_PROFILE > "$module-$port.log" 2>&1 &

  sleep 3
  echo "✅ $module 启动完成！"
}

# ===================== 批量部署所有项目 =====================
for module in "${!PROJECT_MAP[@]}"; do
  ports=${PROJECT_MAP[$module]}
  for port in $ports; do
    kill_by_port "$port"
    start_project "$module" "$port"
  done
done

echo ""
echo "====================================="
echo " ✅ 所有项目部署完成！"
echo "====================================="