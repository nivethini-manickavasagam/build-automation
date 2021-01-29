import '../App.css';
import React from 'react';
import Select from 'react-select';
import 'bootstrap/dist/css/bootstrap.min.css';
import Button from 'react-bootstrap/Button';
import BranchService from '../services/BranchService';
import axios from 'axios';
import DataTable from './DataTableComponent';


const environments =[
  {value: 'qa', label: 'QA' , name: 'selectedEnv'},
  {value: 'dev-02', label: 'Dev 02', name: 'selectedEnv'},
  {value: 'dev-03', label: 'Dev 03', name: 'selectedEnv'},
  {value: 'mth', label: 'MTH', name: 'selectedEnv'},
  {value: 'integration', label: 'Integration', name: 'selectedEnv'},
  {value: 'validation', label: 'Validation', name: 'selectedEnv'},
  {value: 'production', label: 'Production', name: 'selectedEnv'}
]
const modules=[
  {value: 'crypto-lib.bdnl', label: 'crypto-lib.bdnl', name: 'selectedModule'},
  {value: 'gateway-audio-en.bdnl', label: 'gateway-audio-en.bdnl', name: 'selectedModule'},
  {value: 'gateway-audio-es.bdnl', label: 'gateway-audio-es.bdnl', name: 'selectedModule'},
  {value: 'gateway-audio-tones.bdnl', label: 'gateway-audio-tones.bdnl', name: 'selectedModule'},
  {value: 'gateway-config.bdnl', label: 'gateway-config.bdnl', name: 'selectedModule'},
  {value: 'gateway-config-sd.bdnl', label: 'gateway-config-sd.bdnl', name: 'selectedModule'},
  {value: 'gateway-config-ui.bdnl', label: 'gateway-config-ui.bdnl', name: 'selectedModule'},
  {value: 'gateway-ems.bdnl', label: 'gateway-ems.bdnl', name: 'selectedModule'},
  {value: 'gateway-manager-core.bdnl', label: 'gateway-manager-core.bdnl', name: 'selectedModule'},
  {value: 'gw-init-functions.bdnl', label: 'gw-init-functions.bdnl', name: 'selectedModule'},
  {value: 'gw-mount-media.bdnl', label: 'gw-mount-media.bdnl', name: 'selectedModule'}
]
const gateways=[
  {value: 'FMC-GW-SL-600-1', label: 'FMC-GW-SL-600-1', name: 'selectedGateway'},
  {value: 'FMC-GW-SL-600-2', label: 'FMC-GW-SL-600-2', name: 'selectedGateway'},
  {value: 'FMC-GW-SL-600-3', label: 'FMC-GW-SL-600-3', name: 'selectedGateway'},
  {value: 'FMC-GW-SL-600-4', label: 'FMC-GW-SL-600-4', name: 'selectedGateway'},
  {value: 'FMC-GW-SL-600-5', label: 'FMC-GW-SL-600-5', name: 'selectedGateway'},
]
class MainComponent extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
        selectedVersion: null,
        selectedEnv: null,
        selectedModule: null,
        selectedGateway: [],
        branches: [],
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleChangeGateway = this.handleChangeGateway.bind(this);
    this.componentDidMount= this.componentDidMount.bind(this);
  }

  componentDidMount(){
    this.getBranches();

  }

  async getBranches(){
    BranchService.getBranches().then((response) => {
      this.setState({branches : response.data.split(',')})
    });
  }

  async build(){
    var headers = {
      "Access-Control-Allow-Origin": "http://localhost:9090",
      'Content-Type': 'application/json' 
    };
    const requirements = {
      "environment" : this.state.selectedEnv , 
      "version": this.state.selectedVersion
    };
    axios.post("http://localhost:9090/build", requirements, headers)
    .then(res => {
      console.log(res);
      console.log(res.data);
    })
  }

  handleChange(event) {
      console.log(event)
      this.setState({[event.name]: event});
    }    

    handleChangeGateway(event){
        console.log(event)
        this.setState({selectedGateway: [...event]});
    }  

    handleClick = event => {
      event.preventDefault();
      
      this.build();

      console.log("Button clicked.");
    }
  render() {
    const { selectedVersion, selectedEnv, selectedGateway, selectedModule, branches } = this.state;

    return (
      <div>
        <div className= "bg-dark">
          <h4 style={{padding: '15px', color: 'white'}}>Compliance Analyzer</h4>
        </div>
        <br/><br/>

        <div className="box">
          <div className="container-flex">
            <h5>Version </h5>
            <Select className="dropdown"
              value={selectedVersion}
              onChange={this.handleChange}
              options ={branches.map(t=>({value: t, label: t, name: "selectedVersion"}))}
            />

            <h5>Environment</h5>
            <Select className="dropdown"
              value={selectedEnv}
              onChange={this.handleChange}
              options={environments}
            />
          </div>
          <br/>
          <div className="container-flex">
            <h5>Module</h5>
            <Select className="dropdown"
              value={selectedModule}
              onChange={this.handleChange}
              options={modules}
            />

            <h5>Gateway</h5>
            <Select className="dropdown" 
                value={selectedGateway}
                onChange={this.handleChangeGateway}
                isMulti
                options={gateways}
            />
          </div>
          <br/>
          <div className="row">
							<div className="col-md-4"></div>
							<div className="col-md-4">
                <Button variant="primary" size="lg" onClick={this.handleClick}>Build</Button>
              </div>
					    <div className="col-md-4"></div>
          </div>

          <hr/>
          <h3 style={{textAlign: 'center'}}>Build Status</h3>

          <DataTable/>
        </div>
      </div>
    );
  }
}

export default MainComponent;
