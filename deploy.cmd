npm install
npm run release
aws s3 cp resources/public s3://roulette-reframe/ --recursive