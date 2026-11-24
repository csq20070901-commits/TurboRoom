#!/usr/bin/env node
/**
 * TurboRoom 接口自动化测试集
 * 用法：node api-test-suite.mjs [BASE_URL]
 * 默认 BASE_URL=http://localhost:8888（脚本会自动拼上 /meetroom 前缀）
 * 前置条件：后端已启动、MySQL dev 库已就绪（内置账号 student/123456、admin/123456）
 */
const BASE = (process.argv[2] || 'http://localhost:8888').replace(/\/+$/, '') + '/meetroom';

let passed = 0;
let failed = 0;
const failures = [];

function check(name, ok, detail = '') {
  if (ok) {
    passed++;
    console.log(`  PASS  ${name}`);
  } else {
    failed++;
    failures.push(name);
    console.log(`  FAIL  ${name}  ${detail}`);
  }
}

async function post(path, body = {}, headers = {}) {
  const r = await fetch(BASE + path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...headers },
    body: JSON.stringify(body)
  });
  let j = null;
  try { j = await r.json(); } catch {}
  return { http: r.status, code: j && Number(j.code), msg: j && j.msg, data: j && j.data };
}

async function waitUp() {
  for (let i = 0; i < 40; i++) {
    try {
      const r = await post('/home/list');
      if (r.http === 200) return;
    } catch {}
    await new Promise(r => setTimeout(r, 1500));
  }
  throw new Error('后端服务 30 秒内未就绪：' + BASE);
}

await waitUp();
console.log(`TurboRoom 接口自动化测试集\n目标: ${BASE}\n`);

// ============ 1. 公共接口 ============
console.log('[1] 公共接口');
{
  const r = await post('/home/list', {});
  check('home/list 返回 200', r.http === 200 && r.code === 200, `http=${r.http} code=${r.code}`);
  check('home/list 返回 data 对象', r.data && typeof r.data === 'object', JSON.stringify(r.data).slice(0, 60));

  const s = await post('/setup/get', { key: 'SETUP_CONTENT_ABOUT' });
  check('setup/get 返回 200', s.http === 200 && s.code === 200, `http=${s.http} code=${s.code} msg=${s.msg}`);

  const n = await post('/news/list', { page: 1, size: 5, isTotal: true });
  check('news/list 返回 200', n.http === 200 && n.code === 200, `code=${n.code}`);
  check('news/list 返回列表数组', n.data && Array.isArray(n.data.list), JSON.stringify(n.data).slice(0, 60));

  if (n.data && Array.isArray(n.data.list) && n.data.list.length > 0) {
    const nv = await post('/news/view', { id: n.data.list[0].newsId });
    check('news/view 返回 200', nv.http === 200 && nv.code === 200, `code=${nv.code} msg=${nv.msg}`);
  }

  const m = await post('/meet/list', { page: 1, size: 5, isTotal: true });
  check('meet/list 返回 200', m.http === 200 && m.code === 200, `code=${m.code}`);
  check('meet/list 返回列表数组', m.data && Array.isArray(m.data.list), JSON.stringify(m.data).slice(0, 60));

  if (m.data && Array.isArray(m.data.list) && m.data.list.length > 0) {
    const mv = await post('/meet/view', { id: m.data.list[0].meetId });
    check('meet/view 返回 200', mv.http === 200 && mv.code === 200, `code=${mv.code} msg=${mv.msg}`);
  }
}

// ============ 2. 学生端认证与鉴权 ============
console.log('[2] 学生端认证与鉴权');
let custToken = '';
{
  const bad = await post('/passport/login', { account: 'student', password: 'wrong-password-xyz' });
  check('密码错误登录被拒绝(1600)', bad.http === 200 && bad.code === 1600, `code=${bad.code} msg=${bad.msg}`);

  const missing = await post('/passport/login', { account: 'student' });
  check('缺少参数登录被拒绝(1301)', missing.http === 200 && missing.code === 1301, `code=${missing.code} msg=${missing.msg}`);

  const ok = await post('/passport/login', { account: 'student', password: '123456' });
  check('学生登录成功(200)', ok.http === 200 && ok.code === 200, `code=${ok.code} msg=${ok.msg}`);
  custToken = ok.data && ok.data.token && ok.data.token.token;
  check('学生登录返回 token', typeof custToken === 'string' && custToken.length > 10, 'token 缺失');

  const noTok = await post('/passport/my/detail', {});
  check('未登录访问个人信息被拦截(2401)', noTok.http === 200 && noTok.code === 2401, `code=${noTok.code}`);

  if (custToken) {
    const detail = await post('/passport/my/detail', {}, { sub: 'cust', token: custToken });
    check('登录后个人信息返回 200', detail.http === 200 && detail.code === 200, `code=${detail.code} msg=${detail.msg}`);
    check('个人信息包含 userAccount', detail.data && detail.data.userAccount === 'student', JSON.stringify(detail.data).slice(0, 60));
  }

  const fake = await post('/passport/my/detail', {}, { sub: 'cust', token: 'not-a-real-token' });
  check('伪造 token 被拦截(2401)', fake.http === 200 && fake.code === 2401, `code=${fake.code}`);
}

// ============ 3. AI 助手参数校验 ============
console.log('[3] AI 助手参数校验');
{
  const blank = await post('/ai/chat', { question: '' }, { sub: 'cust', token: custToken });
  check('AI 问答空问题被拒绝(1600)', blank.http === 200 && blank.code === 1600, `code=${blank.code} msg=${blank.msg}`);

  const long = await post('/ai/chat', { question: 'x'.repeat(2001) }, { sub: 'cust', token: custToken });
  check('AI 问答超长问题被拒绝(1600)', long.http === 200 && long.code === 1600, `code=${long.code}`);

  const rec = await post('/ai/recommend', {}, { sub: 'cust', token: custToken });
  check('AI 推荐缺参数被拒绝(1600)', rec.http === 200 && rec.code === 1600, `code=${rec.code} msg=${rec.msg}`);

  const noTok = await post('/ai/chat', { question: '你好' });
  check('AI 问答未登录被拦截(2401)', noTok.http === 200 && noTok.code === 2401, `code=${noTok.code}`);

  const noTokRec = await post('/ai/recommend', {});
  check('AI 推荐未登录被拦截(2401)', noTokRec.http === 200 && noTokRec.code === 2401, `code=${noTokRec.code}`);

  const suspended = await post('/ai/suspended-chat', { question: '' });
  check('悬浮问答空问题被拒绝(1600)', suspended.http === 200 && suspended.code === 1600, `code=${suspended.code} msg=${suspended.msg}`);
}

// ============ 4. 管理端认证 ============
console.log('[4] 管理端认证');
let adminToken = '';
{
  const bad = await post('/admin/login', { name: 'admin', password: 'wrong-password' });
  check('管理员密码错误被拒绝(1600)', bad.http === 200 && bad.code === 1600, `code=${bad.code} msg=${bad.msg}`);

  const ok = await post('/admin/login', { name: 'admin', password: '123456' });
  check('管理员登录成功(200)', ok.http === 200 && ok.code === 200, `code=${ok.code} msg=${ok.msg}`);
  adminToken = ok.data && ok.data.token;
  check('管理员登录返回 token', typeof adminToken === 'string' && adminToken.length > 10, 'token 缺失');

  if (adminToken) {
    const mgrList = await post('/admin/mgr/list', { page: 1, size: 10, isTotal: true }, { sub: 'admin', token: adminToken });
    check('管理员访问管理员列表(200)', mgrList.http === 200 && mgrList.code === 200, `code=${mgrList.code} msg=${mgrList.msg}`);
  }

  if (custToken) {
    const custMgr = await post('/admin/mgr/list', { page: 1, size: 10, isTotal: true }, { sub: 'cust', token: custToken });
    check('学生 token 访问管理员列表被拦截(2401)', custMgr.http === 200 && custMgr.code === 2401, `code=${custMgr.code}`);
  }
}

// ============ 5. AI 测试中心 ============
console.log('[5] AI 测试中心');
{
  const noTok = await post('/admin/aitest/regression', {});
  check('测试中心未登录被拦截(2401)', noTok.http === 200 && noTok.code === 2401, `code=${noTok.code}`);

  const custRole = await post('/admin/aitest/regression', {}, { sub: 'cust', token: custToken });
  check('学生 token 访问管理接口被拦截(2401)', custRole.http === 200 && custRole.code === 2401, `code=${custRole.code}`);

  const reg = await post('/admin/aitest/regression', {}, { sub: 'admin', token: adminToken });
  check('获取内置回归套件(200)', reg.http === 200 && reg.code === 200, `code=${reg.code} msg=${reg.msg}`);
  const cases = reg.data && reg.data.cases;
  check('回归套件包含 9 条用例', Array.isArray(cases) && cases.length === 9, `实际 ${cases && cases.length}`);

  if (Array.isArray(cases)) {
    const run = await post('/admin/aitest/run', { cases }, { sub: 'admin', token: adminToken });
    check('执行回归套件返回 200', run.http === 200 && run.code === 200, `code=${run.code} msg=${run.msg}`);
    const summary = run.data && run.data.summary;
    check('回归套件 9/9 全部通过', summary && summary.total === 9 && summary.pass === 9, JSON.stringify(summary));
    check('执行结果逐条返回', run.data && Array.isArray(run.data.results) && run.data.results.length === 9, 'results 缺失');
  }

  const emptyRun = await post('/admin/aitest/run', {}, { sub: 'admin', token: adminToken });
  const emptySummary = emptyRun.data && emptyRun.data.summary;
  check('空用例默认执行内置回归', emptyRun.http === 200 && emptyRun.code === 200 && emptySummary && emptySummary.total === 9, JSON.stringify(emptySummary));

  const missing = await post('/admin/aitest/generate', {}, { sub: 'admin', token: adminToken });
  check('生成用例缺需求描述被拒绝(1301)', missing.http === 200 && missing.code === 1301, `code=${missing.code} msg=${missing.msg}`);
}

// ============ 汇总 ============
console.log(`\n结果: ${passed} 通过, ${failed} 失败, 共 ${passed + failed} 项断言`);
if (failures.length > 0) {
  console.log('失败项:');
  for (const f of failures) console.log('  - ' + f);
  process.exit(1);
}
console.log('全部通过');
