export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  azureAdB2C: {
    tenantName: 'TransporteDuoc',
    clientId: 'cd4c34a8-35fe-45bd-8e99-d286165a10d9',
    policyName: 'B2C_1_susi',
    scopeUri: 'https://TransporteDuoc.onmicrosoft.com/a1f68f5a-8087-4cbf-903f-c2be08caf6c3/demo.read',
    redirectUri: 'http://localhost:4200/'
  }
};
