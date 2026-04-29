<template>
  <div class="page">
    <h2>简历组装 - {{ resume?.title }}</h2>
    <p style="color: #999; margin-bottom: 20px">长按拖拽排序模块，点击模块名称切换素材</p>

    <el-row :gutter="20">
      <el-col :span="10">
        <el-card header="模块开关 / 排序">
          <div ref="sortableEl" class="section-list">
            <div
              v-for="s in sections"
              :key="s.sectionType"
              class="section-item"
              :class="{ active: currentSection === s.sectionType }"
              @mousedown="onItemMouseDown(s)"
            >
              <span class="drag-icon">
                <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor" style="pointer-events:none">
                  <path d="M3 15h2v2H3zm0-4h2v2H3zm0-4h2v2H3zm4 8h2v2H7zm0-4h2v2H7zm0-4h2v2H7zm4 8h2v2h-2zm0-4h2v2h-2zm0-4h2v2h-2zm4 8h2v2h-2zm0-4h2v2h-2zm0-4h2v2h-2z"/>
                </svg>
              </span>
              <el-switch v-model="s.enabled" @mousedown.stop />
              <span class="section-name" @click.stop="currentSection = s.sectionType">{{ sectionLabel(s.sectionType) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card :header="'勾选素材 - ' + sectionLabel(currentSection)">
          <div v-if="currentSection === 'education'">
            <el-checkbox-group v-model="selectedEducation">
              <div v-for="e in educations" :key="e.id" class="checkbox-item">
                <el-checkbox :value="e.id">{{ e.school }} - {{ e.major }} ({{ e.degree }})</el-checkbox>
              </div>
            </el-checkbox-group>
            <p v-if="!educations.length" style="color: #999">暂无教育经历，请先在素材仓库中添加</p>
          </div>
          <div v-else-if="currentSection === 'project_experience'">
            <el-checkbox-group v-model="selectedProjects">
              <div v-for="p in projects" :key="p.id" class="checkbox-item">
                <el-checkbox :value="p.id">{{ p.projectName }} - {{ p.role }}</el-checkbox>
              </div>
            </el-checkbox-group>
            <p v-if="!projects.length" style="color: #999">暂无项目经历，请先在素材仓库中添加</p>
          </div>
          <div v-else-if="currentSection === 'work_experience'">
            <el-checkbox-group v-model="selectedWorks">
              <div v-for="w in works" :key="w.id" class="checkbox-item">
                <el-checkbox :value="w.id">{{ w.company }} - {{ w.position }}</el-checkbox>
              </div>
            </el-checkbox-group>
            <p v-if="!works.length" style="color: #999">暂无工作经历，请先在素材仓库中添加</p>
          </div>
          <div v-else-if="currentSection === 'certificates'">
            <el-checkbox-group v-model="selectedCertificates">
              <div v-for="c in certificates" :key="c.id" class="checkbox-item">
                <el-checkbox :value="c.id">{{ c.name }}</el-checkbox>
              </div>
            </el-checkbox-group>
            <p v-if="!certificates.length" style="color: #999">暂无证书奖项，请先在素材仓库中添加</p>
          </div>
          <div v-else>
            <p style="color: #999">基本信息 / 技能 / 自我评价 — 无需勾选具体素材，模块开启后自动带入</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div style="margin-top: 20px; text-align: center">
      <el-button type="primary" size="large" @click="handleSave" :loading="saving">保存配置</el-button>
      <el-button size="large" @click="$router.push(`/resume/${resumeId}/preview`)">预览简历</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { assembleResume, configureSections } from '@/api/resume'
import { getEducations, getProjects, getWorks, getCertificates } from '@/api/material'
import Sortable from 'sortablejs'
import { ElMessage } from 'element-plus'

const route = useRoute()
const resumeId = Number(route.params.id)
const resume = ref<any>(null)
const sections = ref<any[]>([])
const saving = ref(false)
const sortableEl = ref<HTMLElement>()
let sortableInstance: Sortable | null = null
let dragStarted = false

const educations = ref<any[]>([])
const projects = ref<any[]>([])
const works = ref<any[]>([])
const certificates = ref<any[]>([])
const currentSection = ref('education')
const selectedEducation = ref<number[]>([])
const selectedProjects = ref<number[]>([])
const selectedWorks = ref<number[]>([])
const selectedCertificates = ref<number[]>([])

const SECTION_LABELS: Record<string, string> = {
  basic_info: '基本信息', education: '教育经历', project_experience: '项目经历',
  work_experience: '工作经历', skills: '技能标签', certificates: '证书奖项',
  self_evaluation: '自我评价',
}

function sectionLabel(type: string) { return SECTION_LABELS[type] || type }

function onItemMouseDown(s: any) {
  dragStarted = false
}

onMounted(async () => {
  const [r, edu, proj, work, cert]: any[] = await Promise.all([
    assembleResume(resumeId), getEducations(), getProjects(), getWorks(), getCertificates()
  ])
  resume.value = r.data?.resume
  sections.value = r.data?.sections || []
  educations.value = edu.data || []
  projects.value = proj.data || []
  works.value = work.data || []
  certificates.value = cert.data || []

  for (const sec of sections.value) {
    const ids: number[] = sec.itemIds || []
    switch (sec.sectionType) {
      case 'education': selectedEducation.value = ids; break
      case 'project_experience': selectedProjects.value = ids; break
      case 'work_experience': selectedWorks.value = ids; break
      case 'certificates': selectedCertificates.value = ids; break
    }
  }

  await nextTick()
  await nextTick()
  initSortable()
})

onUnmounted(() => {
  if (sortableInstance) {
    sortableInstance.destroy()
    sortableInstance = null
  }
})

function initSortable() {
  if (!sortableEl.value) return
  if (sortableInstance) sortableInstance.destroy()

  sortableInstance = Sortable.create(sortableEl.value, {
    animation: 150,
    forceFallback: true,
    ghostClass: 'ghost',
    chosenClass: 'chosen',
    dragClass: 'dragging',
    onStart() {
      dragStarted = true
    },
    onEnd(evt) {
      dragStarted = false
      if (evt.oldIndex != null && evt.newIndex != null && evt.oldIndex !== evt.newIndex) {
        const moved = sections.value.splice(evt.oldIndex, 1)[0]
        sections.value.splice(evt.newIndex, 0, moved)
      }
    },
  })
}

async function handleSave() {
  saving.value = true
  try {
    const configs = sections.value.map((s, idx) => ({
      sectionType: s.sectionType,
      enabled: s.enabled,
      sortOrder: idx,
      itemIds: getItemIdsForSection(s.sectionType),
    }))
    await configureSections(resumeId, configs)
    ElMessage.success('配置已保存')
  } finally { saving.value = false }
}

function getItemIdsForSection(type: string): number[] {
  const map: Record<string, number[]> = {
    education: selectedEducation.value,
    project_experience: selectedProjects.value,
    work_experience: selectedWorks.value,
    certificates: selectedCertificates.value,
  }
  return map[type] || []
}
</script>

<style scoped>
.section-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.section-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border-radius: 6px;
  border: 2px solid transparent;
  user-select: none;
  -webkit-user-select: none;
  -webkit-user-drag: none;
  touch-action: none;
}
.section-item:hover {
  background: #f5f7fa;
}
.section-item.active {
  background: #ecf5ff;
  border-color: #409eff;
}
.section-name {
  flex: 1;
  cursor: pointer;
}
.checkbox-item {
  padding: 4px 0;
}
.drag-icon {
  cursor: grab;
  display: inline-flex;
  align-items: center;
  flex-shrink: 0;
  padding: 2px;
  color: #909399;
}
.drag-icon:active {
  cursor: grabbing;
}
.ghost {
  opacity: 0.4;
}
.chosen {
  background: #f0f9ff;
}
.dragging {
  opacity: 0.9;
  box-shadow: 0 2px 12px rgba(0,0,0,0.15);
}
</style>