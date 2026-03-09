-- 更稳健的限流脚本，兼容历史数据：
-- 1) 非数值旧值重置为 0 并设置 TTL
-- 2) 无过期时间（TTL=-1）视为迁移场景，重置为 0 并设置 TTL
local key = KEYS[1]
local val = redis.call('get', key)
local limit = tonumber(ARGV[1]) or 0
local period = tonumber(ARGV[2]) or 60

if val then
    local ttl = redis.call('ttl', key)
    local current = tonumber(val)
    if not current or ttl == -1 then
        -- 历史脏值或无TTL：重置并设置过期，避免计数不归零
        redis.call('set', key, '0')
        redis.call('expire', key, period)
        current = 0
        val = '0'
    end
    if current > limit then
        return current
    end
end

-- 递增计数
local newCount = redis.call('incr', key)
if tonumber(newCount) == 1 then
    -- 第一次调用开始限流，设置过期时间
    redis.call('expire', key, period)
end
return newCount