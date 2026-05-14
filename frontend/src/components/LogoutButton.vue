<template>
  <button class="futuristic-btn">
    <span>Cerrar sesión</span>
  </button>
</template>

<script setup>
</script>

<style scoped>
.futuristic-btn {
  position: relative;
  padding: 0.45em 1.4em;
  font-size: 14px;
  font-weight: 600;
  background: white;
  color: #e11d48;
  border: 4px solid #e11d48;
  cursor: pointer;
  overflow: hidden;
  transition: color 0.3s ease;
  clip-path: polygon(
    0 0,
    100% 0,
    100% 78%,
    92% 100%,
    0 100%,
    0 22%
  );
}

.futuristic-btn span {
  position: relative;
  z-index: 2;
}

.futuristic-btn::before {
  content: "";
  position: absolute;
  top: 50%;
  left: 50%;
  width: 8%;
  height: 500%;
  background: white;
  transform: translate(-50%, -50%) rotate(-60deg);
  transition: all 0.3s ease;
  z-index: 1;
}

.futuristic-btn:hover::before {
  width: 100%;
  background: #e11d48;
  transform: translate(-50%, -50%) rotate(-90deg);
}

.futuristic-btn:hover {
  color: white;
}

/* MOVIL */
@media (max-width: 768px) {
  .futuristic-btn {
    color: white;
  }

  .futuristic-btn::before {
    width: 100%;
    background: #e11d48;
    transform: translate(-50%, -50%) rotate(-90deg);
  }
}
</style>