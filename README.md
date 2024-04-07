# 💡 완료 기능

### 1. 인증

- JWT 검증

  - Nest측 발급 토큰 검증 → 권한별 (Admin / User) 접근 권한 부여

### 2. 아이템

- 아이템 생성 `(POST) /v2/admin/items`
- 아이템 조회 (전체) `(GET) /v2/admin/items`
- 아이템 조회 (단건) `(GET) /v2/admin/items/{itemId}`
- 아이템 수정 `(PATCH) /v2/admin/items/{itemId}`
- 아이템 삭제 `(DELETE) /v2/admin/items/{itemId}`
- 아이템 사용 `(POST) /v2/item-inventory/{itemInventoryId}`


### 3. 캐릭터

- 캐릭터 생성 `(POST) /admin/characters`
- 캐릭터 조회 (전체) `(GET) /admin/characters`
- 캐릭터 조회 (단건) `(GET) /admin/characters/{characterId}`
- 캐릭터 조회 (등급별) `(GET) /characters`
- 캐릭터 수정 `(PATCH) /admin/characters/{characterId}`
- 캐릭터 삭제 `(DELETE) /admin/characters/{characterId}`


### 4. 팔레트

- 팔레트 생성 `(POST) /admin/palettes`
- 팔레트 조회 (전체) `(GET) /admin/palettes`
- 팔레트 조회 (단건) `(GET) /admin/palettes/{paletteId}`
- 팔레트 수정 `(PATCH) /admin/palettes/{paletteId}`
- 팔레트 삭제 `(DELETE) /admin/palettes/{paletteId}`

### 5. 멤버

- 멤버 조회 (전체) `(GET) /admin/members`

### 6. 상점

- 아이템 구매 `(POST) /v2/shop/purchase`
- 캐릭터 판매 `(POST) /v2/shop/sell`

### 7. 타이머

- 아이템 Progress 줄이기 `(POST) /item-inventory/apply-time`



<br><br>

# 📦DB 환경

### 1. Production 환경

- AWS RDS - MySQL(이창우)


### 2. Dev & Test 환경

- AWS RDS - MySQL(이지선)


<br><br>

# 💻 Spring 서버 배포 환경

### 1. AWS EC2

### 2. Git Actions
- build
- deploy

### 3. Docker-compose
- app
- nginx
- certbot


<br><br>

# 📝 Test Code

- API E2E Test (전체 기능)


<br><br>

# ❗미완료 작업

### 1. 캐싱 처리
- Spring 내부에서 처리하는 기능 관련된 내용만 캐싱 처리
- 적용 : 아이템, 팔레트, 캐릭터

### 2. 로깅 처리



<br><br>

# ⭐기능 추가 고려

### 1. 캐릭터 도감

- 전체 캐릭터 중 모은 캐릭터 확인 할 수 있는 도감 생성
- 캐릭터 태그 작성하여, 도감 그룹 생성 고려

### 2. 이모지 추가

- '함께 공부하기' 에서 사용할 수 있는 이모지 추가
