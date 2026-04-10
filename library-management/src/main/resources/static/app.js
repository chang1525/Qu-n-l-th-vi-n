/**
 * Hệ Thống Quản Lý Thư Viện - Frontend Application
 * Kết nối với Spring Boot REST API
 */

const API_BASE = '/api';

// ============================
// Global State
// ============================
let cachedMembers = [];
let cachedDocuments = [];
let cachedTransactions = [];

// ============================
// Utility Functions
// ============================

function showToast(message, type = 'info') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;

    const icons = { success: '✅', error: '❌', info: 'ℹ️' };
    toast.innerHTML = `<span>${icons[type] || 'ℹ️'}</span> <span>${message}</span>`;
    container.appendChild(toast);

    toast.addEventListener('click', () => removeToast(toast));
    setTimeout(() => removeToast(toast), 5000);
}

function removeToast(toast) {
    if (toast.classList.contains('removing')) return;
    toast.classList.add('removing');
    setTimeout(() => toast.remove(), 300);
}

function showLoading() {
    document.getElementById('loadingOverlay').classList.remove('hidden');
}

function hideLoading() {
    document.getElementById('loadingOverlay').classList.add('hidden');
}

async function apiRequest(url, options = {}) {
    const token = localStorage.getItem('auth_token');
    
    const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json;charset=UTF-8',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(url, {
        headers,
        ...options
    });

    if (response.status === 401 || response.status === 403) {
        localStorage.removeItem('auth_token');
        localStorage.removeItem('auth_user');
        window.location.href = 'login.html';
        throw new Error('Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại!');
    }

    const contentType = response.headers.get('content-type') || '';
    let data;

    if (contentType.includes('application/json')) {
        data = await response.json();
    } else {
        data = await response.text();
    }

    if (!response.ok) {
        // Extract message from JSON error response
        const errorMsg = (data && typeof data === 'object' && data.message)
            ? data.message
            : (typeof data === 'string' ? data : `Lỗi HTTP ${response.status}`);
        throw new Error(errorMsg);
    }

    return data;
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function formatDate(dateStr) {
    if (!dateStr) return '—';
    const d = new Date(dateStr + 'T00:00:00');
    return d.toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' });
}

function formatCurrency(amount) {
    if (!amount || amount === 0) return '0đ';
    return new Intl.NumberFormat('vi-VN').format(amount) + 'đ';
}

// ============================
// Navbar
// ============================
function initNavbar() {
    const navbar = document.getElementById('navbar');
    const mobileMenuBtn = document.getElementById('mobileMenuBtn');
    const navLinks = document.getElementById('navLinks');
    const links = document.querySelectorAll('.nav-link');

    window.addEventListener('scroll', () => {
        navbar.classList.toggle('scrolled', window.scrollY > 50);
        updateActiveNavLink();
    });

    mobileMenuBtn.addEventListener('click', () => {
        mobileMenuBtn.classList.toggle('active');
        navLinks.classList.toggle('open');
    });

    links.forEach(link => {
        link.addEventListener('click', () => {
            mobileMenuBtn.classList.remove('active');
            navLinks.classList.remove('open');
        });
    });
}

function updateActiveNavLink() {
    const sections = ['hero', 'search', 'borrow', 'return', 'history'];
    const links = document.querySelectorAll('.nav-link');

    let currentSection = 'hero';
    sections.forEach(id => {
        const el = document.getElementById(id);
        if (el && el.getBoundingClientRect().top <= 200) {
            currentSection = id;
        }
    });

    links.forEach(link => {
        link.classList.toggle('active', link.dataset.section === currentSection);
    });
}

// ============================
// Data Loaders
// ============================
async function loadMembers() {
    try {
        cachedMembers = await apiRequest(`${API_BASE}/members`);
        renderMembersList();
        populateMemberSelect();
        animateCounter(document.getElementById('totalMembers'), cachedMembers.length);
    } catch (err) {
        console.error('Load members error:', err);
    }
}

async function loadDocumentsForSelect() {
    try {
        cachedDocuments = await apiRequest(`${API_BASE}/search`);
        populateDocumentSelect();
        animateCounter(document.getElementById('totalDocuments'), cachedDocuments.length);
    } catch (err) {
        console.error('Load documents error:', err);
    }
}

async function loadTransactions() {
    try {
        cachedTransactions = await apiRequest(`${API_BASE}/transactions`);
        renderTransactionsTable();
        populateReturnSelect();
        animateCounter(document.getElementById('totalTransactions'), cachedTransactions.length);
    } catch (err) {
        console.error('Load transactions error:', err);
    }
}

// ============================
// Members List Rendering & Creation
// ============================
function renderMembersList() {
    const container = document.getElementById('membersList');
    if (!cachedMembers.length) {
        container.innerHTML = '<div class="loading-text">Chưa có độc giả</div>';
        return;
    }

    container.innerHTML = cachedMembers.map(m => {
        const initials = (m.fullName || 'N/A').split(' ').map(w => w[0]).slice(-2).join('').toUpperCase();
        const canBorrow = m.currentBorrowCount < m.maxBorrowLimit;
        return `
            <div class="member-item">
                <div class="member-avatar">${initials}</div>
                <div class="member-info">
                    <div class="member-name">${escapeHtml(m.fullName)}</div>
                    <div class="member-meta">ID: ${m.id} • ${m.username || ''}</div>
                </div>
                <span class="member-status ${canBorrow ? 'ok' : 'limit'}">
                    ${m.currentBorrowCount}/${m.maxBorrowLimit}
                </span>
            </div>
        `;
    }).join('');
}

function initAddMember() {
    const form = document.getElementById('addMemberForm');
    if (!form) return;
    
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const fullName = document.getElementById('newMemberName').value.trim();
        const username = document.getElementById('newMemberUsername').value.trim();
        const email = document.getElementById('newMemberEmail').value.trim();
        const btn = document.getElementById('addMemberBtn');
        
        btn.disabled = true;
        btn.textContent = '⏳ Đang cấp thẻ...';
        
        try {
            const result = await apiRequest(`${API_BASE}/members`, {
                method: 'POST',
                body: JSON.stringify({ fullName, username, email })
            });
            
            showToast(result.message || 'Cấp thẻ thành công! Mật khẩu mặc định là 123456.', 'success');
            form.reset();
            
            // Reload members explicitly
            await loadMembers();
        } catch (err) {
            showToast(err.message || 'Không thể tạo thẻ.', 'error');
        } finally {
            btn.disabled = false;
            btn.textContent = '+ Cấp Thẻ Thư Viện';
        }
    });
}

// ============================
// Select Populators
// ============================
function populateMemberSelect() {
    const select = document.getElementById('borrowMemberId');
    select.innerHTML = '<option value="">-- Chọn độc giả --</option>'
        + cachedMembers.map(m =>
            `<option value="${m.id}">${m.fullName} (ID: ${m.id}) — Đã mượn: ${m.currentBorrowCount}/${m.maxBorrowLimit}</option>`
        ).join('');
}

function populateDocumentSelect() {
    const select = document.getElementById('borrowDocumentId');
    const available = cachedDocuments.filter(d => d.availableCopies > 0);
    select.innerHTML = '<option value="">-- Chọn tài liệu --</option>'
        + available.map(d => {
            const typeConfig = getDocTypeConfig(d.documentType);
            return `<option value="${d.id}">${typeConfig.icon} ${d.title} — Còn ${d.availableCopies} bản</option>`;
        }).join('');
}

function populateReturnSelect() {
    const select = document.getElementById('returnTransactionId');
    // Filter by returnDate being null (not returned yet) instead of Vietnamese status string
    const active = cachedTransactions.filter(t => !t.returnDate);

    if (active.length === 0) {
        select.innerHTML = '<option value="">-- Không có giao dịch nào đang mượn --</option>';
        return;
    }

    select.innerHTML = '<option value="">-- Chọn giao dịch cần trả --</option>'
        + active.map(t =>
            `<option value="${t.id}">GD #${t.id}: ${t.memberName} — ${t.documentTitle} (Hạn: ${formatDate(t.dueDate)})</option>`
        ).join('');
}

// ============================
// Search
// ============================
function initSearch() {
    const searchForm = document.getElementById('searchForm');
    const clearBtn = document.getElementById('clearSearchBtn');

    searchForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        await performSearch();
    });

    clearBtn.addEventListener('click', () => {
        document.getElementById('searchKeyword').value = '';
        document.getElementById('searchAuthor').value = '';
        document.getElementById('searchCategory').value = '';
        document.getElementById('searchYear').value = '';
        document.getElementById('resultsContainer').style.display = 'none';
    });

    performSearch();
}

async function performSearch() {
    const keyword = document.getElementById('searchKeyword').value.trim();
    const author = document.getElementById('searchAuthor').value.trim();
    const category = document.getElementById('searchCategory').value.trim();
    const year = document.getElementById('searchYear').value.trim();

    const params = new URLSearchParams();
    if (keyword) params.append('keyword', keyword);
    if (author) params.append('author', author);
    if (category) params.append('category', category);
    if (year) params.append('year', year);

    const searchBtn = document.getElementById('searchBtn');
    searchBtn.disabled = true;
    searchBtn.textContent = '⏳ Đang tìm...';

    try {
        const queryString = params.toString();
        const url = `${API_BASE}/search${queryString ? '?' + queryString : ''}`;
        const results = await apiRequest(url);

        displayResults(results);
    } catch (error) {
        showToast('Không thể tìm kiếm. Vui lòng kiểm tra kết nối server.', 'error');
        console.error('Search error:', error);
    } finally {
        searchBtn.disabled = false;
        searchBtn.textContent = '🔍 Tìm Kiếm';
    }
}

function displayResults(documents) {
    const container = document.getElementById('resultsContainer');
    const grid = document.getElementById('resultsGrid');
    const noResults = document.getElementById('noResults');
    const countEl = document.getElementById('resultsCount');

    container.style.display = 'block';
    countEl.textContent = `${documents.length} kết quả`;

    if (documents.length === 0) {
        grid.style.display = 'none';
        noResults.style.display = 'block';
        return;
    }

    grid.style.display = 'grid';
    noResults.style.display = 'none';

    grid.innerHTML = documents.map((doc, index) => {
        const typeConfig = getDocTypeConfig(doc.documentType);
        const isAvailable = doc.availableCopies > 0;

        return `
            <div class="doc-card" style="animation: fadeInUp 0.5s ease ${index * 0.06}s both;">
                <div class="doc-card-header">
                    <span class="doc-type-badge ${typeConfig.class}">
                        ${typeConfig.icon} ${typeConfig.label}
                    </span>
                    <span class="doc-card-id">ID: ${doc.id}</span>
                </div>
                <h3 class="doc-card-title">${escapeHtml(doc.title)}</h3>
                <div class="doc-card-meta">
                    <div class="doc-meta-item">
                        <span class="doc-meta-icon">✍️</span>
                        <span>${escapeHtml(doc.author)}</span>
                    </div>
                    <div class="doc-meta-item">
                        <span class="doc-meta-icon">📂</span>
                        <span>${escapeHtml(doc.category)}</span>
                    </div>
                    <div class="doc-meta-item">
                        <span class="doc-meta-icon">📅</span>
                        <span>Năm ${doc.publicationYear}</span>
                    </div>
                </div>
                <div class="doc-card-footer">
                    <div class="copies-badge ${isAvailable ? 'available' : 'unavailable'}">
                        <span class="copies-dot ${isAvailable ? 'available' : 'unavailable'}"></span>
                        ${isAvailable ? `Còn ${doc.availableCopies} bản` : 'Hết bản sao'}
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

function getDocTypeConfig(type) {
    const configs = {
        'BOOK': { icon: '📖', label: 'Sách', class: 'book' },
        'MAGAZINE': { icon: '📰', label: 'Tạp chí', class: 'magazine' },
        'JOURNAL': { icon: '📋', label: 'Tập san', class: 'journal' }
    };
    return configs[type] || { icon: '📄', label: type || 'Khác', class: 'book' };
}

// ============================
// Borrow
// ============================
function initBorrow() {
    document.getElementById('borrowForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const memberId = document.getElementById('borrowMemberId').value;
        const documentId = document.getElementById('borrowDocumentId').value;

        if (!memberId || !documentId) {
            showToast('Vui lòng chọn độc giả và tài liệu.', 'error');
            return;
        }

        const borrowBtn = document.getElementById('borrowBtn');
        borrowBtn.disabled = true;
        borrowBtn.textContent = '⏳ Đang xử lý...';

        try {
            const result = await apiRequest(`${API_BASE}/borrow/${memberId}/${documentId}`, {
                method: 'POST'
            });

            showToast((result.message || 'Mượn tài liệu thành công!') + ' 🎉', 'success');

            document.getElementById('borrowForm').reset();

            // Reload data
            await Promise.all([loadDocumentsForSelect(), loadMembers(), loadTransactions(), performSearch()]);
        } catch (error) {
            showToast(error.message || 'Không thể mượn tài liệu.', 'error');
        } finally {
            borrowBtn.disabled = false;
            borrowBtn.textContent = '📖 Xác Nhận Mượn';
        }
    });
}

// ============================
// Return
// ============================
function initReturn() {
    document.getElementById('returnForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const transactionId = document.getElementById('returnTransactionId').value;

        if (!transactionId) {
            showToast('Vui lòng chọn giao dịch cần trả.', 'error');
            return;
        }

        const returnBtn = document.getElementById('returnBtn');
        returnBtn.disabled = true;
        returnBtn.textContent = '⏳ Đang xử lý...';

        try {
            const result = await apiRequest(`${API_BASE}/borrow/return/${transactionId}`, {
                method: 'POST'
            });

            showToast((result.message || 'Trả tài liệu thành công!') + ' 🎉', 'success');

            document.getElementById('returnForm').reset();

            // Reload data
            await Promise.all([loadDocumentsForSelect(), loadMembers(), loadTransactions(), performSearch()]);
        } catch (error) {
            showToast(error.message || 'Không thể trả tài liệu.', 'error');
        } finally {
            returnBtn.disabled = false;
            returnBtn.textContent = '📥 Xác Nhận Trả Sách';
        }
    });
}

// ============================
// Transactions Table
// ============================
function renderTransactionsTable() {
    const tbody = document.getElementById('transactionsBody');
    const noResults = document.getElementById('noTransactions');

    if (!cachedTransactions.length) {
        tbody.innerHTML = '';
        noResults.style.display = 'block';
        return;
    }

    noResults.style.display = 'none';

    // Sort by ID descending (newest first)
    const sorted = [...cachedTransactions].sort((a, b) => b.id - a.id);

    tbody.innerHTML = sorted.map(t => {
        const isBorrowing = !t.returnDate;
        const fineClass = t.fineAmount > 0 ? 'fine-amount' : 'fine-amount zero';

        return `
            <tr>
                <td><strong>#${t.id}</strong></td>
                <td>${escapeHtml(t.memberName)}</td>
                <td>${escapeHtml(t.documentTitle)}</td>
                <td>${formatDate(t.borrowDate)}</td>
                <td>${formatDate(t.dueDate)}</td>
                <td>${formatDate(t.returnDate)}</td>
                <td class="${fineClass}">${formatCurrency(t.fineAmount)}</td>
                <td>
                    <span class="status-badge ${isBorrowing ? 'borrowing' : 'returned'}">
                        ${isBorrowing ? '📖 Đang mượn' : '✅ Đã trả'}
                    </span>
                </td>
            </tr>
        `;
    }).join('');
}

// ============================
// Stats Counter Animation
// ============================
function animateCounter(element, target) {
    if (!element) return;
    const start = parseInt(element.textContent) || 0;
    if (start === target) { element.textContent = target; return; }

    const duration = 600;
    const startTime = performance.now();

    function update(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        const eased = 1 - Math.pow(1 - progress, 3);
        element.textContent = Math.round(start + (target - start) * eased);
        if (progress < 1) requestAnimationFrame(update);
    }

    requestAnimationFrame(update);
}

// ============================
// Scroll Animations
// ============================
function initScrollAnimations() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1, rootMargin: '0px 0px -50px 0px' });

    document.querySelectorAll('.glass-card, .action-card, .section-header, .table-card, .members-card').forEach(el => {
        el.classList.add('fade-in-up');
        observer.observe(el);
    });
}

// ============================
// Initialize Application
// ============================

function checkAuth() {
    const token = localStorage.getItem('auth_token');
    const userStr = localStorage.getItem('auth_user');
    const authMenu = document.getElementById('authMenu');
    
    if (token && userStr) {
        let user = null;
        try { user = JSON.parse(userStr); } catch(e){}
        
        if (user) {
            authMenu.innerHTML = `
                <span style="font-size: 0.9rem; font-weight: 600; color: var(--primary-light);">Xin chào, ${Object.values(user)[0] || 'Member'}</span>
                <button onclick="logout()" class="btn btn-outline btn-sm">Đăng Xuất</button>
            `;
            return true;
        }
    }
    
    authMenu.innerHTML = '<a href="login.html" class="btn btn-primary btn-sm" id="navLoginBtn">Đăng Nhập</a>';
    return false;
}

window.logout = function() {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_user');
    window.location.reload();
};

document.addEventListener('DOMContentLoaded', async () => {
    setTimeout(hideLoading, 800);

    const isAuthenticated = checkAuth();
    
    initNavbar();
    initSearch();
    initBorrow();
    initReturn();
    initAddMember();
    initScrollAnimations();

    // Refresh history button
    document.getElementById('refreshHistoryBtn').addEventListener('click', loadTransactions);

    // Load all data
    await Promise.all([loadMembers(), loadDocumentsForSelect(), loadTransactions()]);

    console.log('📚 Hệ Thống Quản Lý Thư Viện đã sẵn sàng!');
});
