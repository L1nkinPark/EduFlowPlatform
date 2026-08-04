(function () {
    'use strict';

    function normalizeControl(control) {
        if (!control.value || typeof control.value.normalize !== 'function') return;

        const original = control.value;
        const normalized = original.normalize('NFC');
        if (normalized === original) return;

        const start = control.selectionStart;
        const end = control.selectionEnd;
        const normalizedStart = start == null ? null : original.slice(0, start).normalize('NFC').length;
        const normalizedEnd = end == null ? null : original.slice(0, end).normalize('NFC').length;

        control.value = normalized;
        if (document.activeElement === control && normalizedStart != null && normalizedEnd != null) {
            control.setSelectionRange(normalizedStart, normalizedEnd);
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        const controls = document.querySelectorAll('[data-normalize-unicode]');
        controls.forEach(function (control) {
            normalizeControl(control);
            control.addEventListener('compositionend', function () { normalizeControl(control); });
            control.addEventListener('blur', function () { normalizeControl(control); });
        });

        document.querySelectorAll('form').forEach(function (form) {
            form.addEventListener('submit', function () {
                form.querySelectorAll('[data-normalize-unicode]').forEach(normalizeControl);
            });
        });
    });
}());
