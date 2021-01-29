import axios from 'axios'

const BRANCH_URL="/branch";

class BranchService{

    getBranches(){
        return axios.get(BRANCH_URL);
    }
}

export default new BranchService();