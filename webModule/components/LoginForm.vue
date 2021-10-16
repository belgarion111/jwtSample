<template>
  <v-form
    ref="form"
    v-model="valid"
    lazy-validation
  >
    <v-text-field
      v-model="login.username"
      :counter="10"
      :rules="nameRules"
      label="Name"
      required
    ></v-text-field>

    <v-text-field
      :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
      v-model="login.password"
      :rules="passwordRules"
      :type="show ? 'text' : 'password'"
      label="Password"
      hint="At least 8 characters"
      @click:append="show = !show"
    ></v-text-field>

    <v-btn
      :disabled="!valid"
      color="success"
      class="mr-4"
      @click="validate"
    >
      Validate
    </v-btn>

    <v-btn
      color="error"
      class="mr-4"
      @click="reset"
    >
      Reset Form
    </v-btn>

    <v-btn
      color="warning"
      @click="resetValidation"
    >
      Reset Validation
    </v-btn>
    <v-btn
      color="primary"
      @click.prevent="userLogin">
      Login
    </v-btn>

    <!--
          nuxt
      to="/inspire"
    -->
  </v-form>
</template>

<script>
export default {
  name: "LoginForm",
  data: () => ({
    valid: true,
    login: {
      username: '',
      password: ''
    },
    show: false,
    nameRules: [
      v => !!v || 'Name is required',
      v => (v && v.length <= 10) || 'Name must be less than 10 characters',
    ],
    passwordRules: [
      v => !!v || 'Password is required',
      v => (v && v.length <= 10) || 'Password must be less than 10 characters',
    ],
    select: null
  }),

  methods: {
    validate () {
      this.$refs.form.validate()
    },
    reset () {
      this.$refs.form.reset()
    },
    resetValidation () {
      this.$refs.form.resetValidation()
    },
    async userLogin() {
      try {
        //this.$axios.$post(`/api/login`,{data: this.login});
        //console.log(response);
        let response = await this.$auth.loginWith('local', {data: this.login})
        if(this.$auth.loggedIn){
          this.$router.push({ name: 'inspire' })
        }
      } catch (err) {
        console.log(err)
      }

    }
  },
}
</script>

<style scoped>

</style>
