// .releaserc.js

const { releasePackage } = require('@akijoey/semantic-release-config')

module.exports = releasePackage('maven', {
  profiles: 'release'
})
