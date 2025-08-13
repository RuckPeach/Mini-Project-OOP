<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="th">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ระบบจัดการคิวบุฟเฟ่</title>
    <link rel="stylesheet" href="css/style.css">
    <script src="js/main.js" defer></script>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🍽️ ระบบจัดการคิวบุฟเฟ่</h1>
            <div class="stats">
                <div class="stat-card">
                    <h3>${manager.totalPeople} คน</h3>
                    <p>จำนวนลูกค้าทั้งหมด</p>
                </div>
                <div class="stat-card">
                    <h3>${manager.waitingCount}</h3>
                    <p>คิวรอ</p>
                </div>
                <div class="stat-card">
                    <h3>${manager.eatingCount}</h3>
                    <p>กำลังทาน (โต๊ะ)</p>
                </div>
                <div class="stat-card">
                    <h3>${manager.availableTables}</h3>
                    <p>โต๊ะว่าง</p>
                </div>
                <div class="stat-card">
                    <h3>${manager.totalServed}</h3>
                    <p>รับบริการแล้ว (โต๊ะ)</p>
                </div>
            </div>
        </div>

        <div class="main-content">
            <div class="section">
                <h2>📋 คิวรอรับบริการ</h2>
                <div class="queue-controls">
                    <form action="buffet" method="post" class="add-queue-form">
                        <input type="hidden" name="action" value="addQueue">
                        <input type="text" class="form-input" placeholder="ชื่อลูกค้า" name="customerName" required autocomplete="off">
                        <input type="tel" class="form-input" placeholder="เบอร์โทรศัพท์" name="customerPhone" required autocomplete="off">
                        <input type="number" class="form-input" placeholder="จำนวน" name="customerCount" min="1" max="20" required>
                        <div class="BTN">
                            <button type="submit" class="btn btn-success">เพิ่มคิว</button>
                        </div>
                    </form>
                </div>

                <div id="queueList">
                    <c:forEach items="${manager.queue}" var="item">
                        <div class="queue-item" data-join-time="${item.joinedAtMillis}">
                            <div class="queue-info">
                                <div class="queue-number">${item.id}</div>
                                <div class="queue-details">${item.name} (${item.phone}) - ${item.count} ท่าน - <span class="wait-time"></span></div>
                            </div>
                            <div class="queue-actions">
                                <form action="buffet" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="assignTable">
                                    <input type="hidden" name="queueId" value="${item.id}">
                                    <button type="submit" class="btn btn-success">จัดโต๊ะ</button>
                                </form>
                                <form action="buffet" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="cancelQueue">
                                    <input type="hidden" name="queueId" value="${item.id}">
                                    <button type="submit" class="btn btn-danger">ยกเลิก</button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="section">
                <h2>🪑 สถานะโต๊ะ</h2>
                <div class="tables-grid">
                    <c:forEach items="${manager.tables}" var="table">
                        <div class="table-card ${fn:toLowerCase(table.status.name())}"
                            data-status="${table.status}"
                            data-start-time="${table.startTimeMillis}"
                            data-table-id="${table.id}">
                            <div class="table-number">โต๊ะ ${table.id}</div>
                            <div class="table-capacity">รองรับ ${table.capacity} ท่าน</div>
                            <div class="table-status">
                                <c:choose>
                                    <c:when test="${table.status == 'OCCUPIED'}">
                                        ${table.customerName} (${table.customerCount} ท่าน)
                                    </c:when>
                                    <c:otherwise>
                                        ${table.status.displayName}
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="table-time">
                                <c:if test="${table.status != 'AVAILABLE'}">
                                    ${table.elapsedMinutes} นาที
                                </c:if>
                            </div>
                            <div class="table-actions">
                                <c:if test="${table.status == 'OCCUPIED'}">
                                    <form action="buffet" method="post">
                                        <input type="hidden" name="action" value="freeTable">
                                        <input type="hidden" name="tableId" value="${table.id}">
                                        <button type="submit" class="btn btn-warning">เก็บโต๊ะ</button>
                                    </form>
                                </c:if>
                                <c:if test="${table.status == 'CLEANING'}">
                                    <form action="buffet" method="post">
                                        <input type="hidden" name="action" value="finishCleaning">
                                        <input type="hidden" name="tableId" value="${table.id}">
                                        <button type="submit" class="btn btn-primary">พร้อมใช้</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
