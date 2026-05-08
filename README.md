
# 📋 Project: In-Memory Blog System (Zero-Config)

> **데이터베이스 설치 없이 즉시 실행 가능한 자바 기반 블로그 시스템**

본 프로젝트는 복잡한 RDBMS 설치 과정 없이, Java의 자료구조만을 활용하여 데이터의 관계성과 영속성 로직을 구현한 **Zero-Config** 블로그 애플리케이션입니다.

---

## 1. 프로젝트 목적 (Overview)

* **Zero-Config:** DB 연동 과정에서 발생하는 오버헤드를 제거하여 실행 즉시 서비스를 이용할 수 있는 환경 구축.
* **In-Memory Architecture:** RDBMS의 역할을 Java의 `Collections` 프레임워크로 대체하여 메모리 기반의 고속 데이터 처리 및 관계형 데이터 모델링 학습.

---

## 2. 기술 스택 (Tech Stack)

* **Framework:** Spring Boot 4.x, Java 25
* **View:** Thymeleaf, Layout Dialect
* **Storage:** Java Collections (ConcurrentHashMap, ArrayList)
* **Concurrency Control:** AtomicLong (ID 생성 보안 및 동시성 보장)
* **Utility:** LoremIpsum (테스트용 더미 데이터 자동 생성)

---

## 3. 핵심 설계 및 기능 (Key Features)

### 📂 In-Memory Storage 설계

* `Map<Long, Post>` 및 `Map<Long, Category>` 구조를 사용하여 O(1)의 데이터 접근 속도를 확보했습니다.
* 멀티스레드 환경을 고려하여 `ConcurrentHashMap`과 `AtomicLong`을 사용, 데이터 무결성을 보장합니다.

### 🏷️ 카테고리 관리 시스템

* **일괄 편집:** 여러 카테고리의 이름과 순서를 한 번의 요청으로 수정할 수 있는 벌크 업데이트 로직을 구현했습니다.
* **데이터 보호 정책:** 특정 카테고리 삭제 시, 해당 카테고리에 속한 게시글들은 자동으로 **'미분류(Unclassified)'** 카테고리로 이동되어 데이터 유실을 방지합니다.

### 💬 무제한 트리 구조 대댓글 (Recursive Comments)

* **재귀 로직:** 부모 댓글 ID(Parent ID)를 추적하여 계층형 구조를 형성합니다.
* **Depth 무제한:** 별도의 깊이 제한 없이 트리 구조로 댓글이 렌더링되도록 구현했습니다.

### 📄 오프셋 기반 페이징 (Slicing)

* 메모리 상의 List를 가공하여 **Offset**과 **Limit** 기반의 페이징 로직을 직접 구현했습니다.
* '이전/다음' 버튼을 통해 전체 데이터를 슬라이싱하여 보여주는 유연한 UI를 제공합니다.

---


## 💡 Insight

> "DB가 없는 환경에서도 데이터의 **원자성(Atomicity)**과 **관계(Relationship)**를 어떻게 유지할 것인가?"에 대한 고민을 Java의 고급 자료구조 활용을 통해 해결하며 백엔드 데이터 처리의 본질을 학습했습니다.
