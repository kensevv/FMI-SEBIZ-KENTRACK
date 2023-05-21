<template>
  <q-page padding>
    <div class="text-center">
      <h3>Welcome to <img src="/src/assets/logo.png"></h3>
    </div>
    <div class="row items-start q-gutter-md">
      <q-card class="q-pa-md col">
        <img
            src="/src/assets/pr-tool.png"
            :height="250"
        >
        <q-card-section>
          <div class="text-h6">Enjoy powerful project management</div>
          <div class="text-subtitle2">A project management tool that can be adapted to your processes to help you
            deliver great products. Track projects and tasks, use agile boards, plan sprints and releases, keep a
            knowledge base, work with reports and dashboards, create workflows that follow your business processes.
            Never force your process to fit the limits of a tool again. Unlike other project management tools, <b>KenTrack</b>
            can be customized to your needs!
          </div>
        </q-card-section>
      </q-card>
      <q-card class="q-pa-md col">
        <img
            src="/src/assets/boost-prod.png"
            :height="250"
        >
        <q-card-section>
          <div class="text-h6">Boost the productivity of every team in your company</div>
          <div class="text-subtitle2">KenTrack has the flexibility to adapt not just to every team, but to every team
            member. From personal tasks to company-wide portfolios of projects, KenTrack can handle it all with ease
            and elegance.

            Boost your productivity with the full complement of issue management features for software development
            teams.
            s
          </div>
        </q-card-section>
      </q-card>

      <q-card class="q-pa-md col">
        <img
            src="/src/assets/teamwork.png"
            :height="250"
        >
        <q-card-section>
          <div class="text-h6">Work together as a team</div>
          <div class="text-subtitle2">Keep communication open and collaborate with teammates through comments and
            reactions. Choose how you want to stay informed about project updates: emails, the in-app Notification
            Center, or a Slack channel. Mention your teammate in a discussion to involve them, and subscribe to tasks
            to get up-to-the-minute issue and article updates. KenTrack allows you to customize the workflows to fit
            your specific processes; for instance, it is possible to set events in your projects that when triggered
            will notify specific people.
          </div>
        </q-card-section>
      </q-card>
    </div>
      <q-page-container>
        <section style="min-height: 25vh;" class="flex flex-center layout_bg">
          <div style="position: relative">
            <div class="text-h4 text-center">
              Pick the best plan for you
            </div>
          </div>
        </section>
        <section class="q-pb-lg">
          <div class="row q-col-gutter-sm q-px-sm">
            <div class="col" v-for="pricing_item in pricing_data">
              <card-pricing :title="pricing_item.title" :icon="pricing_item.icon" :price="pricing_item.price"
                            :background_image="pricing_item.background_image" :text="pricing_item.text"></card-pricing>
            </div>
          </div>
        </section>
      </q-page-container>
  </q-page>
</template>

<script lang="ts" setup>
</script>

<script>
import {defineComponent, defineAsyncComponent} from 'vue'

const pricing_data = [
  {
    title: 'Basic Free trial',
    price: '$0',
    icon: 'home_work',
    background_image: 'linear-gradient(to right, #ed6ea0 0%, #ec8c69 100%)',
    text: '1 month free trial supporting up to 2 people.'
  },
  {
    title: 'Small Company',
    price: '$50',
    icon: 'home',
    background_image: 'linear-gradient(-225deg, #5D9FFF 0%, #6BBBFF 100%)',
    text: 'This is good if your company size is between 2 and 20 people.'
  },
  {
    title: 'Extended',
    price: '$250',
    icon: 'apartment',
    background_image: 'linear-gradient(to right, #2B86C5 0%, #2B86C5 100%)',
    text: 'This is good if your company size above 20 people. (Up to 100)'
  },
]
export default defineComponent({
  name: "Pricing",
  components: {
    CardPricing: defineAsyncComponent(() => import('../components/CardPricing.vue'))
  },
  setup() {
    return {
      year: (new Date()).getFullYear(),
      pricing_data
    }
  }
})
</script>

<style scoped>

</style>